package com.rich.sol_bot.user.withdraw;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.sol.entity.Transaction;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLog;
import com.rich.sol_bot.user.withdraw.mapper.UserWithdrawLogRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MainWithdrawSchedule {
    public String generateLockKey(String type) {
        return redisKeyGenerator.generateKey("MainWithdrawSchedule", type);
    };


    @Scheduled(fixedDelay = 1000)
    public void start() {
        if(lockKey("start")){
//            log.info("MainWithdrawSchedule 代币提现: start");
            List<UserWithdrawLog> logs = userWithdrawLogRepository.list(new LambdaQueryWrapper<UserWithdrawLog>()
                    .eq(UserWithdrawLog::getState, TradeStateEnum.created).last("limit 1"));
            for(UserWithdrawLog i : logs) {
                if(userWithdrawLogRepository.update(new LambdaUpdateWrapper<UserWithdrawLog>()
                                .set(UserWithdrawLog::getState, TradeStateEnum.submit)
                        .eq(UserWithdrawLog::getState, TradeStateEnum.created)
                        .eq(UserWithdrawLog::getId, i.getId())
                )){
                    String txId = withdrawSubmitService.submitTransfer(i);
                    if(txId != null) {
                        userWithdrawLogRepository.update(new LambdaUpdateWrapper<UserWithdrawLog>()
                                .set(UserWithdrawLog::getTxid, txId).isNull(UserWithdrawLog::getTxid)
                                        .set(UserWithdrawLog::getSubmitAt, TimestampUtil.now())
                                .eq(UserWithdrawLog::getState, TradeStateEnum.submit).eq(UserWithdrawLog::getId, i.getId())
                        );
                    } else {
                        userWithdrawLogRepository.update(new LambdaUpdateWrapper<UserWithdrawLog>()
                                .set(UserWithdrawLog::getState, TradeStateEnum.fail).isNull(UserWithdrawLog::getTxid)
                                .eq(UserWithdrawLog::getState, TradeStateEnum.submit).eq(UserWithdrawLog::getId, i.getId())
                        );
                    }
                }
            }
            unlock("start");
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void confirm() {
        if(lockKey("confirm")){
//            log.info("MainWithdrawSchedule 代币提现: confirm");
            List<UserWithdrawLog> logs = userWithdrawLogRepository.list(new LambdaQueryWrapper<UserWithdrawLog>()
                            .isNotNull(UserWithdrawLog::getTxid)
                    .eq(UserWithdrawLog::getState, TradeStateEnum.submit));
            for(UserWithdrawLog i : logs) {
                Transaction transaction = solQueryService.getTransaction(i.getTxid());
                // 交易成功
                if(transaction != null && transaction.getMeta().getErr() == null) {
                    userWithdrawService.confirmTransfer(i);
                }
                // 交易5min查不到
                if(transaction == null && i.getSubmitAt().compareTo(TimestampUtil.minus(5, TimeUnit.MINUTES)) < 0) {
                    userWithdrawService.failTransfer(i);
                }
                // 交易明确失败
                if(transaction != null && transaction.getMeta().getErr() != null) {
                    userWithdrawService.failTransfer(i);
                }
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
    private RedisKeyGenerateTool redisKeyGenerator;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private UserWithdrawService userWithdrawService;
    @Resource
    private UserWithdrawLogRepository userWithdrawLogRepository;
    @Resource
    private WithdrawSubmitService withdrawSubmitService;
    @Resource
    private SolQueryService solQueryService;
}
