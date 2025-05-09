package com.rich.sol_bot.sniper.mapper;

import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.sniper.enums.SniperStateEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class SniperPlan {
    private Long id;
    private Long uid;
    private Long tokenId;
    private BigDecimal mainAmount;
    private BigDecimal extraGas;
    private SniperMode mode;
    private BigDecimal slippage;
    private BigDecimal liquidity;
    private Integer walletCount;
    private Timestamp createdAt;
    private SniperStateEnum state;
    private Integer deleted;

    public String showMainAmount() {
        return this.mainAmount.stripTrailingZeros().toPlainString();
    }

    public String showExtraGas() {
        return this.extraGas.stripTrailingZeros().toPlainString();
    }

    public String showWalletCount() {
        return this.walletCount.toString();
    }
}
