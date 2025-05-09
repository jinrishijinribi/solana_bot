package com.rich.sol_bot.admin.stat.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.admin.stat.StatService;
import com.rich.sol_bot.admin.stat.enums.ParentUpdateScene;
import com.rich.sol_bot.admin.stat.mapper.StatInvite;
import com.rich.sol_bot.admin.stat.mapper.StatInviteRepository;
import com.rich.sol_bot.admin.stat.mapper.StatTradeDaily;
import com.rich.sol_bot.admin.stat.mapper.StatTradeDailyRepository;
import com.rich.sol_bot.bot.handler.constants.BotReplyDealConstants;
import com.rich.sol_bot.bot.service.BotNotifyService;
import com.rich.sol_bot.iceberg.IcebergService;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.mapper.TradeInfoRepository;
import com.rich.sol_bot.trade.service.TradeSubmitService;
import com.rich.sol_bot.user.mapper.User;
import com.rich.sol_bot.user.mapper.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class StatDailySchedule {

    @Autowired
    private StatTradeDailyRepository statTradeDailyRepository;
    @Autowired
    private StatService statService;
    @Autowired
    private StatInviteRepository statInviteRepository;
    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void start() {
        if(lockKey("generate")){
            StatTradeDaily statTradeDaily = statTradeDailyRepository.getOne(new LambdaQueryWrapper<StatTradeDaily>()
                    .orderByDesc(StatTradeDaily::getId).last("limit 1"));
            if(statTradeDaily == null) return;
            Timestamp nowId = TimestampUtil.getTimestampOfZone(TimestampUtil.now(), 8L);
            if(statTradeDaily.getId() < nowId.getTime()) {
                statService.updateDaily(new Timestamp(statTradeDaily.getId() + 1000 * 60 * 60 * 24));
            }
            unlock("generate");
        }
    }


    @Scheduled(fixedDelay = 3600 * 1000)
    public void inviteLast() {
        if(lockKey("inviteLast")){
            Timestamp todayTs = TimestampUtil.getTimestampOfZone(TimestampUtil.now(), 8L);
            List<StatInvite> items = statInviteRepository.list(new LambdaQueryWrapper<StatInvite>().le(StatInvite::getLastUpdateAt, todayTs).last("limit 5"));
            for(StatInvite item: items) {
                User user = userRepository.getById(item.getId());
                statService.updateStatInvite(user, ParentUpdateScene.all);
            }
            unlock("inviteLast");
        }
    }


    public String generateLockKey(String type) {
        return redisKeyGenerateTool.generateName("StatDailySchedule", type);
    };

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
