package com.rich.sol_bot.sol.queue;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangqiyun
 * @since 2024/3/20 15:47
 */


@Data
@Accessors(chain = true)
public class Message {
    private String pri_key;
    private String tx;
    private Long sniper_plan_tx_id;
}
