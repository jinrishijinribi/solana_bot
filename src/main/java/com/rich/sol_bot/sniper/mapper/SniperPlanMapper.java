package com.rich.sol_bot.sniper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SniperPlanMapper extends BaseMapper<SniperPlan> {

    @Select("select distinct token_id from sniper_plan where state='created' and `deleted`=0")
    List<Long> getToken_idDistinct();

    @Update("update `sniper_plan` set `wallet_count` = `wallet_count` + #{count} where `id` = #{id}")
    long updateWalletCount(Long id, Long count);
}
