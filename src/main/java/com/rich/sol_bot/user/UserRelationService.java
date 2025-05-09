package com.rich.sol_bot.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.admin.stat.StatService;
import com.rich.sol_bot.admin.stat.enums.ParentUpdateScene;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.mapper.TradeInfoRepository;
import com.rich.sol_bot.user.balance.UserBalanceService;
import com.rich.sol_bot.user.balance.log.mapper.UserBalanceLog;
import com.rich.sol_bot.user.balance.log.mapper.UserBalanceLogRepository;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.enums.BalanceLogTypeEnum;
import com.rich.sol_bot.user.mapper.*;
import jakarta.annotation.Resource;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class UserRelationService {
    @Autowired
    private StatService statService;

    @Transactional
    public void calculateTrade(TradeInfo info) {
        Long uid = info.getUid();
        Long parent = userRelationRepository.getParent(uid);
        if(parent == null) return;
        if(userBalanceLogRepository.count(new LambdaQueryWrapper<UserBalanceLog>()
                .eq(UserBalanceLog::getBizId, info.getId()).eq(UserBalanceLog::getUid, info.getUid())
                .eq(UserBalanceLog::getType, BalanceLogTypeEnum.rebate)) == 0){
            User user = userRepository.getById(parent);
            UserConfig userConfig = userConfigRepository.getById(parent);
            BigDecimal rebate = info.getPlatformFee().multiply(userConfig.getRebateRate());
            tradeInfoRepository.update(new LambdaUpdateWrapper<TradeInfo>().set(TradeInfo::getRebate, rebate).eq(TradeInfo::getId, info.getId()));
            info.setRebate(rebate);
            if(info.getRebate() != null && info.getRebate().compareTo(BigDecimal.ZERO) > 0) {
                userBalanceService.changBalanceByType(parent, TokenBaseInfo.mainSymbol(), rebate, BalanceLogTypeEnum.rebate, info.getId());
            }
            ThreadAsyncUtil.execAsync(() -> {
                statService.updateStatInvite(user, ParentUpdateScene.trade);
            });
        }
    }

    @Transactional
    public void tryToBindRelation(User user, String refCode) {
        if(!StringUtil.isBlank(refCode)){
            User p = userRepository.getByRefCode(refCode);
            if(Objects.nonNull(p) && !Objects.equals(p.getId(), user.getId())){
                userRelationRepository.save(UserRelation.builder()
                        .id(user.getId())
                        .pid(p.getId()).createdAt(TimestampUtil.now())
                        .build());
                ThreadAsyncUtil.execAsync(() -> {
                    statService.updateStatInvite(p, ParentUpdateScene.invite);
                });
            }
        }
    }

    @Resource
    private UserBalanceLogRepository userBalanceLogRepository;
    @Resource
    private TradeInfoRepository tradeInfoRepository;
    @Resource
    private UserBalanceService userBalanceService;
    @Resource
    private UserRelationRepository userRelationRepository;
    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserConfigRepository userConfigRepository;
}
