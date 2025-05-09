package com.rich.sol_bot.sol.new_coin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author wangqiyun
 * @since 2024/3/24 17:30
 */

@Mapper
public interface NewCoinMapper extends BaseMapper<NewCoin> {

    @Select("select * from new_coin where (base_mint='So11111111111111111111111111111111111111112' AND quote_mint=#{mint}) OR (base_mint=#{mint} AND quote_mint='So11111111111111111111111111111111111111112')")
    NewCoin get(@Param("mint") String mint);
}
