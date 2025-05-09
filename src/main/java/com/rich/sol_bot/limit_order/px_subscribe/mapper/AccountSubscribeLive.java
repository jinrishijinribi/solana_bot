package com.rich.sol_bot.limit_order.px_subscribe.mapper;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class AccountSubscribeLive {
    private Long id;
    private String ammkey;
    private Long accountSubscribeId;
    private String mint;
    private String address;
    private Long amount;
    private Integer submitId;
    private Timestamp updatedAt;
    private Timestamp lastFailAt;
    private Integer checkFailCount;
    private Timestamp liveUntil;
}
