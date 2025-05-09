package com.rich.sol_bot.system.config;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.io.Serializable;

@Getter
public enum SystemConfigConstant implements Serializable {
    SYSTEM_NAME("system.name", "", false, ""),
    BOT_TOKEN("bot.token", "", false, ""),
    BOT_NAME("bot.name", "", false, ""),
    BOT_WEBHOOK_URL("bot.webhook.url", "", false, ""),
    BOT_WEBHOOK_SECRET_TOKEN("bot.webhook.secret.token", "", false, ""),


    SCRAPER_STATUS("scraper.status", "disable", false, ""),
    TRADE_FEE_RATE("trade.fee.rate", "", false, ""),
    TRADE_FEE_ADDRESS("trade.fee.address", "", false, ""),
    TRADE_REBATE_ADDRESS("trade.rebate.address", "", false, ""),
    TRADE_REBATE_SECRET("trade.rebate.secret", "", false, ""),
    TRADE_REBATE_RATE("trade.rebate.rate", "", false, "")

//    BOT_SYNC_HEIGHT("bot.sync.height", "", false, ""),
//    SYNC_STAKE_FEE("sync.stake.fee", "250", false, ""),
//    SYNC_UNSTAKE_FEE("sync.unstake.fee", "5000", false, ""),
    ;

    @EnumValue
    private final String id;
    private final String defaultValue;
    private final boolean optional;
    private final String description;

    SystemConfigConstant(String id, String defaultValue, boolean optional, String description) {
        this.id = id;
        this.defaultValue = defaultValue;
        this.optional = optional;
        this.description = description;
    }
}
