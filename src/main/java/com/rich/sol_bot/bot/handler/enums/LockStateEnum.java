package com.rich.sol_bot.bot.handler.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LockStateEnum {
    // 等待用户端输入的状态锁

    wallet_create_name("wallet_create_name"),
    wallet_import_name("wallet_import_name"),
    wallet_import_priKey("wallet_import_priKey"),
    deal_to_choose_token("deal_to_choose_token"),
    deal_to_choose_wallet("deal_to_choose_wallet"),
    deal_to_choose_amount("deal_to_choose_amount"),
    deal_to_iceberg_amount("deal_to_iceberg_amount"),

    deal_to_limit_px_buy("deal_to_limit_px_buy"),
    deal_to_limit_px_sell("deal_to_limit_px_sell"),
    deal_to_limit_rate_buy("deal_to_limit_rate_buy"),
    deal_to_limit_rate_sell("deal_to_limit_rate_sell"),


    wallet_change_name("wallet_change_name"),
    wallet_transfer_sol("wallet_transfer_sol"),
    wallet_transfer_input_amount("wallet_transfer_input_amount"),
    wallet_transfer_input_address("wallet_transfer_input_address"),


    // setting
    setting_for_fast("setting_for_fast"),
    setting_for_protect("setting_for_protect"),
    setting_for_buy_gas("setting_for_buy_gas"),
    setting_for_sell_gas("setting_for_sell_gas"),
    setting_for_sniper("setting_for_sniper"),
    setting_for_jito_fee("setting_for_jito_fee"),
    setting_for_auto_sell("setting_for_auto_sell"),


    // sniper
    sniper_input_token("sniper_input_token"),
    sniper_input_amount("sniper_input_amount"),
    sniper_input_extra_gas("sniper_input_extra_gas"),
    sniper_input_liquidity("sniper_input_liquidity"),
    sniper_input_wallet("sniper_input_wallet"),
    sniper_re_input_liquidity("sniper_re_input_liquidity"),
    sniper_re_input_extra_gas("sniper_re_input_extra_gas"),
    sniper_re_input_wallet("sniper_re_input_wallet"),
    sniper_re_input_amount("sniper_re_input_amount"),

    // invite
    invite_input_wallet("invite_input_wallet"),

    // position
    position_to_choose_amount("position_to_choose_amount"),

    // scraper
    scraper_input_twitter_name("scraper_input_twitter_name"),
    scraper_input_amount("scraper_input_amount"),
    scraper_input_count("scraper_input_count"),
    scraper_modify_amount("scraper_modify_amount")
    ;
    @EnumValue
    private final String value;

}
