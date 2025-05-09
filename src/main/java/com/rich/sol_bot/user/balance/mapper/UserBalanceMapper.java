package com.rich.sol_bot.user.balance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface UserBalanceMapper extends BaseMapper<UserBalance> {
    @Update("update `user_balance` set `remain` = `remain` + #{amount}, `amount` = `amount` + #{amount} " +
            "where `uid` = #{uid} and `coin` = #{coin}")
    long increaseRemain(Long uid, String coin, BigDecimal amount);
    @Update("update `user_balance` set `remain` = `remain` - #{amount}, `amount` = `amount` - #{amount} " +
            "where `uid` = #{uid} and `coin` = #{coin} and `remain` >= #{amount}")
    long reduceRemain(Long uid, String coin, BigDecimal amount);
    @Update("update `user_balance` set `remain` = `remain` - #{amount}, `freeze` = `freeze` + #{amount} " +
            "where `uid` = #{uid} and `coin` = #{coin} and `remain` >= #{amount}")
    long freezeRemain(Long uid, String coin, BigDecimal amount);
    @Update("update `user_balance` set `remain` = `remain` + #{amount}, `freeze` = `freeze` - #{amount} " +
            "where `uid` = #{uid} and `coin` = #{coin} and `freeze` >= #{amount}")
    long unfreezeRemain(Long uid, String coin, BigDecimal amount);
    @Update("update `user_balance` set `amount` = `amount` - #{amount}, `freeze` = `freeze` - #{amount} " +
            "where `uid` = #{uid} and `coin` = #{coin} and `freeze` >= #{amount}")
    long reduceFreeze(Long uid, String coin, BigDecimal amount);
    @Update("update `user_balance` set `amount` = `amount` + #{amount}, `freeze` = `freeze` + #{amount} " +
            "where `uid` = #{uid} and `coin` = #{coin} ")
    long increaseFreeze(Long uid, String coin, BigDecimal amount);
}
