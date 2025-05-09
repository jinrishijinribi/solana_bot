package com.rich.sol_bot.sol.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PumpCal {
    private String priorityFee;
    private String owner;
    private String mint;
    private String hold;
    private String amount;
    private String sol;
    private String feeAccount;
    private String fee;
    private Boolean buy;
}
