package com.rich.sol_bot.trade.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyDealConstants;
import com.rich.sol_bot.bot.service.BotNotifyService;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.limit_order.mapper.LimitOrderRepository;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.dto.ConfirmTradeDTO;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.iceberg.IcebergService;
import com.rich.sol_bot.iceberg.mapper.IcebergRepository;
import com.rich.sol_bot.iceberg.mapper.IcebergTask;
import com.rich.sol_bot.iceberg.mapper.IcebergTaskMapper;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.mapper.TradeInfoRepository;
import com.rich.sol_bot.trade.service.TradeSubmitService;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TradeSchedule {
    @Autowired
    private IcebergRepository icebergRepository;
    @Autowired
    private IcebergTaskMapper icebergTaskMapper;
    @Autowired
    private LimitOrderRepository limitOrderRepository;
    @Autowired
    private I18nTranslator i18nTranslator;
    @Autowired
    private UserRepository userRepository;

    public String generateLockKey(String type) {
        return redisKeyGenerateTool.generateName("TradeSchedule", type);
    };

    @Scheduled(fixedDelay = 100)
    public void start() {
        if(lockKey("submit")){
//            log.info("提交交易,TradeSchedule: start: after");
            List<TradeInfo> list = tradeInfoRepository.list(new LambdaQueryWrapper<TradeInfo>()
                    .eq(TradeInfo::getState, TradeStateEnum.created).isNull(TradeInfo::getTx).last("limit 10")
            );
            for(TradeInfo i : list) {
                log.info("start submit{}", i.getId());
                // first check 冰山委托
                if(i.getIceId() != null) {
                    if(!icebergService.icebergCheck(i)){
                        continue;
                    }
                }
                if(tradeInfoRepository.update(new LambdaUpdateWrapper<TradeInfo>()
                                .set(TradeInfo::getState, TradeStateEnum.submit)
                        .eq(TradeInfo::getId, i.getId()).eq(TradeInfo::getState, TradeStateEnum.created))
                ){
                    TradeInfo reTrade = tradeSubmitService.submitTrade(i);
                    I18nLanguageEnum language = userRepository.getLanguage(i.getUid());

                    if(reTrade == null) {
                        tradeInfoRepository.update(new LambdaUpdateWrapper<TradeInfo>()
                                .set(TradeInfo::getState, TradeStateEnum.fail)
                                .eq(TradeInfo::getId, i.getId()).eq(TradeInfo::getState, TradeStateEnum.submit));
                        botNotifyService.pushFailTrade(i, i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealFailContent));
                    } else {
                        String txId = reTrade.getTx();
                        BigDecimal platformFee = reTrade.getPlatformFee();
                        if(txId != null) {
                            tradeInfoRepository.update(new LambdaUpdateWrapper<TradeInfo>()
                                    .set(TradeInfo::getTx, txId).set(TradeInfo::getSubmitAt, TimestampUtil.now())
                                    .set(TradeInfo::getPlatformFee, platformFee).set(TradeInfo::getSource, reTrade.getSource())
                                    .eq(TradeInfo::getId, i.getId()).eq(TradeInfo::getState, TradeStateEnum.submit));
                            if(i.getIceId() != null) {
                                icebergTaskMapper.submitOne(i.getIceId());
                            }
                        } else {
                            tradeInfoRepository.update(new LambdaUpdateWrapper<TradeInfo>()
                                    .set(TradeInfo::getState, TradeStateEnum.fail)
                                    .eq(TradeInfo::getId, i.getId()).eq(TradeInfo::getState, TradeStateEnum.submit));
                            botNotifyService.pushFailTrade(i, i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealFailContent));
                        }
                    }
                }
            }
            unlock("submit");
        }
    }

    @Scheduled(fixedDelay = 100)
    public void confirm() {
        if(lockKey("confirm")){
//            log.info("提交交易,TradeSchedule: confirm");
            List<TradeInfo> list = tradeInfoRepository.list(new LambdaQueryWrapper<TradeInfo>()
                    .eq(TradeInfo::getState, TradeStateEnum.submit).isNotNull(TradeInfo::getTx).last("limit 10")
            );
            for(TradeInfo i : list) {
                ConfirmTradeDTO confirm = tradeSubmitService.confirmTrade(i);
                // 找不到交易
                if(confirm == null && i.getSubmitAt().compareTo(TimestampUtil.minus(2, TimeUnit.MINUTES)) < 0) {
                    // 5分钟找不到交易, 就确认交易失败
                    tradeInfoRepository.update(new LambdaUpdateWrapper<TradeInfo>()
                            .set(TradeInfo::getState, TradeStateEnum.fail)
                            .eq(TradeInfo::getState, TradeStateEnum.submit).eq(TradeInfo::getId, i.getId()));
                    if(i.getIceId() != null){
                        icebergService.failOne(i.getIceId());
                        IcebergTask task = icebergRepository.getById(i.getIceId());
                        if(task.getSuccessCount() + task.getFailCount() == task.getAllCount()){
                            botNotifyService.pushIceAllSuccess(i);
                        }
                    }
                    limitOrderRepository.failOrder(i.getId());
                    I18nLanguageEnum language = userRepository.getLanguage(i.getUid());
                    botNotifyService.pushFailTrade(i, i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealFailContent));
                    continue;
                }
                // 明确的交易失败
                if(confirm != null && !confirm.getSuccess()) {
                    tradeInfoRepository.update(new LambdaUpdateWrapper<TradeInfo>()
                            .set(TradeInfo::getState, TradeStateEnum.fail)
                            .eq(TradeInfo::getState, TradeStateEnum.submit).eq(TradeInfo::getId, i.getId()));
                    if(i.getIceId() != null){
                        icebergService.failOne(i.getIceId());
                        IcebergTask task = icebergRepository.getById(i.getIceId());
                        if(task.getSuccessCount() + task.getFailCount() == task.getAllCount()){
                            botNotifyService.pushIceAllSuccess(i);
                        }
                    }
                    limitOrderRepository.failOrder(i.getId());
                    botNotifyService.pushFailTrade(i, confirm.getErrorMsg());
                    continue;
                }
//                // 确认成功
//                if(confirm != null && confirm.getSuccess()) {
//                    if(i.getIceId() != null){
//                        icebergService.successOne(i.getIceId());
//                    }
//                }
            }
            unlock("confirm");
        }
    }




    public boolean lockKey(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        return lock.setIfAbsent(true, Duration.ofSeconds(60));
    }

    public void unlock(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        lock.delete();
    }

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private TradeSubmitService tradeSubmitService;
    @Resource
    private TradeInfoRepository tradeInfoRepository;
    @Resource
    private BotNotifyService botNotifyService;
    @Resource
    private IcebergService icebergService;
}
