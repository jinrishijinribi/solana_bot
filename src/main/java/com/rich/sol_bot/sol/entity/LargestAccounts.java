package com.rich.sol_bot.sol.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class LargestAccounts {
    private List<Account> accounts;

    @Data
    @Builder
    public static class Account {
        private String address;
        private BigDecimal amount;
        private Integer decimals;
        private BigDecimal uiAmount;
        private BigDecimal uiAmountString;
    }
}
