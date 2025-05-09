package com.rich.sol_bot.user.balance.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserBalanceRepository extends ServiceImpl<UserBalanceMapper, UserBalance> {
    @Transactional
    public UserBalance getOrCreate(Long uid, String coin) {
        UserBalance userBalance = this.getOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUid, uid).eq(UserBalance::getCoin, coin)
        );
        if(userBalance == null) {
            userBalance = UserBalance.builder().id(IdUtil.nextId()).uid(uid)
                    .coin(coin).amount(BigDecimal.ZERO).freeze(BigDecimal.ZERO)
                    .remain(BigDecimal.ZERO).updatedAt(TimestampUtil.now())
                    .build();
            this.save(userBalance);
        }
        return userBalance;
    }

    @Transactional
    public UserBalance getOrCreateMain(Long uid) {
        UserBalance userBalance = this.getOne(new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUid, uid).eq(UserBalance::getCoin, "SOL")
        );
        if(userBalance == null) {
            userBalance = UserBalance.builder().id(IdUtil.nextId()).uid(uid)
                    .coin("SOL").amount(BigDecimal.ZERO).freeze(BigDecimal.ZERO)
                    .remain(BigDecimal.ZERO).updatedAt(TimestampUtil.now())
                    .build();
            this.save(userBalance);
        }
        return userBalance;
    }

}
