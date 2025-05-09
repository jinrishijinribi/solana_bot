package com.rich.sol_bot.limit_order.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LimitOrderPxParam {
    private String ammkey;
    private String mint;
//    private String owner;
    private String amount;
    private String slot;
}
