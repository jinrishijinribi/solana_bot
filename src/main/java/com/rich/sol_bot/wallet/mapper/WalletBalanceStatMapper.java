package com.rich.sol_bot.wallet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Mapper
public interface WalletBalanceStatMapper extends BaseMapper<WalletBalanceStat> {

    @Update("update `wallet_balance_stat` set `amount` = `amount` + #{amount}, `val` = `val` + #{val}, `px` = #{avgPx} where `id` = #{id}")
    long increaseBalance(long id, BigDecimal amount, BigDecimal val, BigDecimal avgPx);

    @Update("update `wallet_balance_stat` set `amount` = `amount` + #{amount}, `val` = `val` + #{val}, `px` = #{avgPx}, `hold_start_at` = #{holdStart} where `id` = #{id}")
    long increaseBalanceAndSetStart(long id, BigDecimal amount, BigDecimal val, BigDecimal avgPx, Timestamp holdStart);

    @Update("update `wallet_balance_stat` set `amount` = `amount` - #{amount}, `val` = `val` - #{val}, `px` = #{avgPx} where `id` = #{id}")
    long decreaseBalance(long id, BigDecimal amount, BigDecimal val, BigDecimal avgPx);
}
