package com.rich.sol_bot.user.config.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class UserConfig {
    private Long id;
    private BigDecimal feeRate;
    private BigDecimal rebateRate;
    private BigDecimal fastSlippage;
    private BigDecimal protectSlippage;
    private BigDecimal buyFee;
    private BigDecimal sellFee;
    private BigDecimal sniperFee;
    private BigDecimal jitoFee;
    private Long preferWallet;
    private Integer autoSell;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public String showFastSlippage() {
        return fastSlippage.movePointRight(2).stripTrailingZeros().toPlainString() + "%";
    }

    public String showProtectSlippage() {
        return protectSlippage.movePointRight(2).stripTrailingZeros().toPlainString() + "%";
    }

    public String showBuyFee() {
        return buyFee.stripTrailingZeros().toPlainString();
    }
    public String showSellFee() {
        return sellFee.stripTrailingZeros().toPlainString();
    }
    public String showSniperFee() {
        return sniperFee.stripTrailingZeros().toPlainString();
    }
    public String showJitoFee() {
        return jitoFee.stripTrailingZeros().toPlainString();
    }

}
