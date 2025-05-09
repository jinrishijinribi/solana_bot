package com.rich.sol_bot.admin.stat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rich.sol_bot.admin.stat.dto.TradeStatDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface StatInviteMapper extends BaseMapper<StatInvite> {

    @Select("select count(*) from `user_relation` where `pid` = #{pid} and `created_at` >= #{start}")
    Long countInvite(Long pid, Timestamp start);

    @Select("select count(distinct a.uid) from `trade_info` a inner join `user_relation` b on a.uid = b.id where b.pid = #{pid}")
    Long validInvite(Long pid);

    @Select("select " +
            "  IFNULL(sum(case a.token_in_id when #{mainId} then a.token_in_amount else a.token_out_amount end),0) as tradeAmount, " +
            " IFNULL(sum(a.rebate),0) as tradeRebate " +
            " from `trade_info` a inner join `user_relation` b on a.uid = b.id " +
            " where b.pid = #{pid} and a.created_at >= #{start} and a.state = 'success'")
    TradeStatDTO tradeAmount(Long pid, Long mainId, Timestamp start);

    @Select("select " +
            "  IFNULL(sum(case a.token_in_id when #{mainId} then a.token_in_amount else a.token_out_amount end),0) as tradeAmount, " +
            " IFNULL(sum(a.rebate),0) as tradeRebate " +
            " from `trade_info` a" +
            " where a.created_at >= #{start} and a.created_at <= #{end} and a.state = 'success'")
    TradeStatDTO tradeAmountOfTimeRange(Long mainId, Timestamp start, Timestamp end);

//
//    @Select("select " +
//            "  IFNULL(sum(case a.token_in_id when #{mainId} then a.token_in_amount else a.token_out_amount end),0) as tradeAmount, " +
//            " IFNULL(sum(a.rebate),0) as tradeRebate " +
//            " from `trade_info` " +
//            " where a.created_at >= #{start} and a.created_at <= #{end} and a.state = 'success'")
//    TradeStatDTO tradeAmountOfTimeRange(Long mainId, Timestamp start, Timestamp end);

}
