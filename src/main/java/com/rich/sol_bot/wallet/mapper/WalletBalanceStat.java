package com.rich.sol_bot.wallet.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

@Data
@Builder
public class WalletBalanceStat {
    private Long id;
    private Long uid;
    private Long tokenId;
    private Long walletId;
    private BigDecimal amount;
    private BigDecimal val;
    private Timestamp updatedAt;
    private BigDecimal px; // 持仓均价
    private Timestamp holdStartAt;

    public String showAmount() {
        return amount.stripTrailingZeros().toPlainString();
    }

    public String usdtValue(BigDecimal price) {
        return amount.multiply(price).stripTrailingZeros().toPlainString();
    }

    public BigDecimal solValue(BigDecimal price, BigDecimal solPrice) {
        return amount.multiply(price).divide(solPrice, RoundingMode.HALF_DOWN);
    }
}
