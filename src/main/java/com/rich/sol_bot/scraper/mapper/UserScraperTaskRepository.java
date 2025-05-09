package com.rich.sol_bot.scraper.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.sniper.enums.SniperMode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserScraperTaskRepository extends ServiceImpl<UserScraperTaskMapper, UserScraperTask> {

    public List<UserScraperTask> getTasksByUid(Long uid) {
        return this.list(new LambdaQueryWrapper<UserScraperTask>()
                .eq(UserScraperTask::getUid, uid)
                .eq(UserScraperTask::getDeleted, 0)
        );
    }

    public void updateCount(Long taskId, Long uid, Long count) {
        this.update(new LambdaUpdateWrapper<UserScraperTask>().set(UserScraperTask::getCount, count)
                .eq(UserScraperTask::getId, taskId).eq(UserScraperTask::getUid, uid));
    }

    public void updateWalletId(Long taskId, Long uid, Long walletId) {
        this.update(new LambdaUpdateWrapper<UserScraperTask>().set(UserScraperTask::getWalletId, walletId)
                .eq(UserScraperTask::getId, taskId).eq(UserScraperTask::getUid, uid));
    }

    public void updateMode(Long taskId, Long uid, SniperMode mode) {
        this.update(new LambdaUpdateWrapper<UserScraperTask>().set(UserScraperTask::getMode, mode)
                .eq(UserScraperTask::getId, taskId).eq(UserScraperTask::getUid, uid));
    }

    public void updateAmount(Long taskId, Long uid, BigDecimal amount) {
        this.update(new LambdaUpdateWrapper<UserScraperTask>().set(UserScraperTask::getAmount, amount)
                .eq(UserScraperTask::getId, taskId).eq(UserScraperTask::getUid, uid));
    }

    public void updateSuccessCount(Long taskId, Long uid, Integer count, Integer successCount) {
        int deleted = count.equals(successCount) ? 1 : 0;
        this.update(new LambdaUpdateWrapper<UserScraperTask>()
                .set(UserScraperTask::getSuccessCount, successCount).set(UserScraperTask::getDeleted, deleted)
                .eq(UserScraperTask::getId, taskId).eq(UserScraperTask::getUid, uid));
    }

    public void removeTask(Long taskId, Long uid) {
        this.update(new LambdaUpdateWrapper<UserScraperTask>().set(UserScraperTask::getDeleted, 1)
                .eq(UserScraperTask::getId, taskId).eq(UserScraperTask::getUid, uid));
    }

    public List<UserScraperTask> listByTwitterUserId(Long twitterUserId) {
        return this.list(new LambdaQueryWrapper<UserScraperTask>()
                .eq(UserScraperTask::getTwitterUserId, twitterUserId)
                .eq(UserScraperTask::getDeleted, 0));
    }
}
