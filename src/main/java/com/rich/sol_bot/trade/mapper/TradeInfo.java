package com.rich.sol_bot.trade.mapper;

import com.rich.sol_bot.trade.enums.TradeDexEnum;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Builder
public class TradeInfo {
    private Long id;
    private Long uid;
    private Long walletId;
    private Long tokenInId;
    private String tokenIn;
    private BigDecimal tokenInAmount;
    private Long tokenOutId;
    private String tokenOut;
    private BigDecimal tokenOutAmount;
    private String tx;
    private BigDecimal extraGas;
    private BigDecimal gas;
    private BigDecimal platformFee;
    private BigDecimal slippage;
    private BigDecimal rebate;
    private TradeStateEnum state;
    private Timestamp createdAt;
    private Timestamp submitAt;
    private BigDecimal mainPx;
    private Integer dedicated;
    private Long iceId;
    private BigInteger minTokenInAmount;
    private TradeDexEnum source;
}
