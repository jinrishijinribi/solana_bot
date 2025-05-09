package com.rich.sol_bot.limit_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface LimitOrderMapper extends BaseMapper<LimitOrder> {

    @Select("select distinct `token_id` from `limit_order` where `state` = 'created' and `expired_at` >= #{expireTime}")
    List<Long> listTokenIds(Timestamp expireTime);
}
