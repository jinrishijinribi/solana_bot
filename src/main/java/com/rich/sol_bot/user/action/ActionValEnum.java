package com.rich.sol_bot.user.action;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionValEnum {
    wallet_name("wallet_name", ActionEnum.wallet),
    wallet_prikey("wallet_prikey", ActionEnum.wallet),
    wallet_change_name_id("wallet_change_name_id", ActionEnum.wallet),
    wallet_transfer_sol_id("wallet_transfer_sol_id", ActionEnum.wallet),
    wallet_transfer_sol_amount("wallet_transfer_sol_amount", ActionEnum.wallet),

    deal_token_address("deal_token_address", ActionEnum.deal),
    deal_token_page("deal_token_page", ActionEnum.deal),
    deal_wallet_id("deal_wallet_id", ActionEnum.deal),
    deal_message_pin("deal_message_pin", ActionEnum.deal),
//    deal_amount("deal_amount", ActionEnum.deal),
    deal_side("deal_side", ActionEnum.deal),
    deal_mode("deal_mode", ActionEnum.deal),
    deal_message_id("deal_message_id", ActionEnum.deal),

    sniper_token_address("sniper_token_address", ActionEnum.sniper),
    sniper_main_amount("sniper_main_amount", ActionEnum.sniper),
    sniper_extra_gas("sniper_extra_gas", ActionEnum.sniper),
    sniper_extra_liquidity("sniper_extra_liquidity", ActionEnum.sniper),
    sniper_plan_id("sniper_plan_id", ActionEnum.sniper),

    position_wallet_id("position_wallet_id", ActionEnum.position),
    position_page("position_page", ActionEnum.position),
    position_token_id("position_token_id", ActionEnum.position),
    position_deal_side("position_deal_side", ActionEnum.position),
    position_deal_amount("position_deal_amount",ActionEnum.position),

    scraper_twitter_username("scraper_twitter_username", ActionEnum.scraper),
    scraper_keyboard_task_id("scraper_keyboard_task_id", ActionEnum.scraper),
    scraper_keyboard_count_task_id("scraper_keyboard_count_task_id", ActionEnum.scraper), // 用户次数统计
    scraper_keyboard_amount_task_id("scraper_keyboard_amount_task_id", ActionEnum.scraper), // 修改金额的时候
    ;
    @EnumValue
    private final String value;
    private final ActionEnum action;
}
