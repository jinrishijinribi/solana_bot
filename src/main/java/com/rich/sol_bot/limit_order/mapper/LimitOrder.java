package com.rich.sol_bot.limit_order.mapper;

import com.rich.sol_bot.limit_order.enums.OrderStatEnum;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.trade.enums.TradeSideEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class LimitOrder {
    private Long id;
    private Long uid;
    private Long tokenId;
    private Long walletId;
    private String tokenName;
    private BigDecimal amount;
    private BigDecimal nowPx;
    private BigDecimal px;
    private TradeSideEnum side;
    private SniperMode mode;
    private BigDecimal slippage;
    private BigDecimal extraGas;
    private OrderStatEnum state;
    private Timestamp createdAt;
    private Timestamp expiredAt;
}
