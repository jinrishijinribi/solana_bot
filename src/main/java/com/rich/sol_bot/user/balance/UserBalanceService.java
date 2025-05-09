package com.rich.sol_bot.user.balance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.user.balance.log.UserBalanceLogService;
import com.rich.sol_bot.user.balance.mapper.UserBalance;
import com.rich.sol_bot.user.balance.mapper.UserBalanceMapper;
import com.rich.sol_bot.user.balance.mapper.UserBalanceRepository;
import com.rich.sol_bot.user.enums.BalanceLogTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserBalanceService {
    @Transactional
    public Boolean changBalanceByType(Long uid, String coin, BigDecimal amount, BalanceLogTypeEnum type, Long bizId) {
        UserBalance userBalance = userBalanceRepository.getOrCreate(uid, coin);
        Long success = 0L;
        switch (type.getAction()){
            case increaseRemain -> {
                success = userBalanceMapper.increaseRemain(uid, coin, amount);
            }
            case reduceRemain -> {
                success = userBalanceMapper.reduceRemain(uid, coin, amount);
            }
            case freezeRemain -> {
                success = userBalanceMapper.freezeRemain(uid, coin, amount);
            }
            case unfreezeRemain -> {
                success = userBalanceMapper.unfreezeRemain(uid, coin, amount);
            }
            case reduceFreeze -> {
                success = userBalanceMapper.reduceFreeze(uid, coin, amount);
            }
            case increaseFreeze -> {
                success = userBalanceMapper.increaseFreeze(uid, coin, amount);
            }
        }
        if(success == 0) {
            return false;
        }
        userBalance = userBalanceRepository.getOrCreate(uid, coin);
        userBalanceLogService.generateLog(userBalance, amount, type, bizId);
        return true;
    }

    public UserBalance getMainUserBalance(Long uid) {
        return userBalanceRepository.getOrCreate(uid, "SOL");
    }


    @Resource
    private UserBalanceRepository userBalanceRepository;
    @Resource
    private UserBalanceMapper userBalanceMapper;
    @Resource
    private UserBalanceLogService userBalanceLogService;
}
