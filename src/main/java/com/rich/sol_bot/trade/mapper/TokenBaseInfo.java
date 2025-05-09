package com.rich.sol_bot.trade.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

@Data
@Builder
public class TokenBaseInfo {
    private Long id;
    private String address;
    private Integer decimals;
    private BigDecimal supply;
    private String symbol;
    private String name;
    private Integer hasPool;
    private String price;
    private String mkValue;
    private String liquidity;
    private Long onbusCount;
    private Integer mintable;
    private Integer dropPermission;
    private Integer liquidityLock;
    private String top10Percent;
    private String twitterUrl;
    private String tgUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long poolStartTime;
    private String url;
    private String image;

    @JsonIgnore
    public Boolean isMain() {
        return "So11111111111111111111111111111111111111112".equals(address);
    }

    @JsonIgnore
    public static String mainAddress() {
        return "So11111111111111111111111111111111111111112";
    }

    @JsonIgnore
    public static String mainSymbol() {
        return "SOL";
    }

    @JsonIgnore
    public static Integer mainDecimals() {
        return 9;
    }

    public String showMint() {
        if(!this.checkPool()) return "暂无";
        return this.mintable == 1 ? "❌": "✅";
    }
    public String showDropPermission() {
        if(!this.checkPool()) return "暂无";
        return this.dropPermission == 0 ? "❌": "✅";
    }
    public String showLiquidityLock() {
        if(!this.checkPool()) return "暂无";
        if(this.liquidityLock == null) return "暂无";
        return this.liquidityLock == 0 ? "❌": "✅";
    }
    public String showTop10Percent() {
        if(this.getTop10Percent() != null){
            return new BigDecimal(this.getTop10Percent()).movePointRight(2)
                    .setScale(2, RoundingMode.HALF_DOWN).toPlainString() + "%";
        }
        return "暂无";
    }
    public String showPrice() {
        if(this.getPrice() != null){
            return new BigDecimal(this.getPrice())
                    .setScale(8, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString();
        }
        return "暂无";
    }
    public String showPool() {
        if(!this.checkPool()) return "暂无";
        return this.symbol + "-SOL";
    }

    public Boolean checkPool() {
        return this.hasPool == 1;
    }

    public String showLiquidity() {
        if(!this.checkPool()) return "暂无";
        return this.liquidity;
    }
}
