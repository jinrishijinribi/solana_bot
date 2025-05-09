package com.rich.sol_bot.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;

@Mapper
public interface TradeInfoMapper extends BaseMapper<TradeInfo> {

    @Select("select count(distinct `uid`) from `trade_info` where `token_in_id` = #{tokenId} and `state` = 'success'")
    long countUidToken(Long tokenId);
    @Select("select count(distinct `uid`) from `trade_info` where `state` = 'success' and `created_at` > #{start} and `created_at` < #{end}")
    long countUid(Timestamp start, Timestamp end);
    @Select("select count(*) from `trade_info` where `state` = 'success' and `created_at` > #{start} and `created_at` < #{end}")
    long countSuccess(Timestamp start, Timestamp end);
}
