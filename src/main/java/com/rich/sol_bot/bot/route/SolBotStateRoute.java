package com.rich.sol_bot.bot.route;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.CheckError;
import com.rich.sol_bot.bot.check.CheckService;
import com.rich.sol_bot.bot.handler.*;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplySniperConstants;
import com.rich.sol_bot.bot.handler.enums.KeyBoardType;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.scraper.mapper.UserScraperTaskRepository;
import com.rich.sol_bot.sniper.SniperGenerateService;
import com.rich.sol_bot.sniper.mapper.SniperPlan;
import com.rich.sol_bot.sniper.mapper.SniperPlanWalletRepository;
import com.rich.sol_bot.trade.enums.TradeSideEnum;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.twitter.TwitterSearchService;
import com.rich.sol_bot.twitter.mapper.TwitterUser;
import com.rich.sol_bot.user.config.UserConfigService;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.user.withdraw.UserWithdrawService;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;

@Service
@Slf4j
public class SolBotStateRoute {
    @Autowired
    private BotDealLimitHandler botDealLimitHandler;
    @Autowired
    private BotScraperHandler botScraperHandler;
    @Autowired
    private UserScraperTaskRepository userScraperTaskRepository;
    @Autowired
    private I18nTranslator i18nTranslator;

    public void handlerWithState(TokenBot bot, Update update, LockStateEnum state, String content) {
        Long uid = update.getMessage().getFrom().getId();
        log.info("{}: state: {}", uid, state.getValue());
        content = content.replace(" ", "");
        switch (state) {
            // 钱包: 输入钱包名字
            case wallet_create_name -> {
                if(!checkService.isNameValid(bot, uid, content)){
                    return;
                }
                botWalletHandler.createWalletSuccess(bot, uid, content);
                botStartHandler.handlerStart(bot, uid, null);
                botStateService.unlockState(uid);
            }
            // 钱包: 输入钱包名字
            case wallet_import_name -> {
                if(!checkService.isNameValid(bot, uid, content)){
                    return;
                }
                botWalletHandler.importWalletPri(bot, uid, content);
                botStateService.lockState(uid, LockStateEnum.wallet_import_priKey);
            }
            // 钱包: 输入钱包私钥
            case wallet_import_priKey -> {
                if(!checkService.isPriKeyValid(bot, uid, content)){
                    return;
                }
                botWalletHandler.importWalletSuccess(bot, uid, content);
                botStartHandler.handlerStart(bot, uid, null);
                botStateService.unlockState(uid);
            }
            // 交易: 合约地址输入
            case deal_to_choose_token -> {
                if(!checkService.checkTokenAddressPass(bot, uid, content)){
                    return;
                };
                userActionService.setValue(uid, ActionValEnum.deal_token_address, content);
                TokenBaseInfo tokenBaseInfo = tokenInfoService.geneTokenBaseInfo(content);
                Long walletId = userWalletService.getMostHoldWalletId(uid, tokenBaseInfo.getId());
                if(walletId == null) {
                    UserWallet userWallet = userWalletService.getPreferOne(uid);
                    if(userWallet != null) {
                        walletId = userWallet.getId();
                    }
                }
                botDealHandler.showKeyBoard(uid, null, tokenBaseInfo.getId(), walletId, 1L, null, false, KeyBoardType.common);
                botStateService.unlockState(uid);
            }
            // 交易: 金额输入
            case deal_to_choose_amount -> {
                if(!checkService.isNumberValid(bot, uid, content, 4)){
                    return;
                };
                String dealMessageId = userActionService.getValue(uid, ActionValEnum.deal_message_id);
                String side = userActionService.getValue(uid, ActionValEnum.deal_side);
                botDealHandler.dealImportAmount(bot, uid, Integer.valueOf(dealMessageId), new BigDecimal(content), TradeSideEnum.valueOf(side), false);
                botStateService.unlockState(uid);
            }
            // 交易: 输入冰山金额
            case deal_to_iceberg_amount -> {
                String dealMessageId = userActionService.getValue(uid, ActionValEnum.deal_message_id);
                String[] contentSplit = content.replace("，", ",").split(",");
                if(contentSplit.length != 2) return;
                String amount = contentSplit[0];
                String count = contentSplit[1];
                if(amount.contains("%")){
                    amount = new BigDecimal(amount.replace("%", "")).movePointLeft(2).stripTrailingZeros().toPlainString();
                }
                if(!checkService.isNumberValid(bot, uid, amount, 4)){
                    return;
                };
                if(!checkService.isNumberValid(bot, uid, count, 0)) {
                    return;
                }
                if(!checkService.isOverCount(bot, uid, count, 10)) {
                    return;
                }
                String side = userActionService.getValue(uid, ActionValEnum.deal_side);
                botDealHandler.dealIcebergAmount(bot, uid, Integer.valueOf(dealMessageId), new BigDecimal(amount), TradeSideEnum.valueOf(side), Long.valueOf(count));
                botStateService.unlockState(uid);
            }
            // 钱包: 设置钱包名称
            case wallet_change_name -> {
                String walletId = userActionService.getValue(uid, ActionValEnum.wallet_change_name_id);
                userWalletService.setWalletName(uid, Long.valueOf(walletId), content);
                botWalletHandler.walletKeyBoard(bot, uid, null);
                botStateService.unlockState(uid);
            }
            // 钱包: 转出sql
            case wallet_transfer_sol -> {
                if(!checkService.isNumberValid(bot, uid, content, 4)){
                    return;
                };
                if(botWalletHandler.handlerTransferSolAmount(bot, uid, content)){
                    botStateService.lockState(uid, LockStateEnum.wallet_transfer_input_address);
                } else {
                    bot.sendMessageToQueue(BotPushMessage.builder()
                            .type(BotMessageTypeEnum.simple_text)
                            .chatId(uid).messageId(null)
                            .text(CheckError.transferSolNotEnough)
                            .build());
                }
            }
            // 钱包: 转出 sql 输入地址
            case wallet_transfer_input_address -> {
                if(!checkService.isUserAddressValid(bot, uid, content)){
                    return;
                };
                botWalletHandler.handlerTransferSolAddress(bot, uid, content);
                botStateService.unlockState(uid);
            }
            // 设置:
            case setting_for_fast -> {
                userConfigService.updateFast(uid, new BigDecimal(content).movePointLeft(2));
                botSettingHandler.settingKeyboard(bot, uid, null);
                botStateService.unlockState(uid);
            }
            case setting_for_protect -> {
                userConfigService.updateProtect(uid, new BigDecimal(content).movePointLeft(2));
                botSettingHandler.settingKeyboard(bot, uid, null);
                botStateService.unlockState(uid);
            }
            case setting_for_buy_gas -> {
                userConfigService.updateBuyFee(uid, new BigDecimal(content));
                botSettingHandler.settingKeyboard(bot, uid, null);
                botStateService.unlockState(uid);
            }
            case setting_for_sell_gas -> {
                userConfigService.updateSellFee(uid, new BigDecimal(content));
                botSettingHandler.settingKeyboard(bot, uid, null);
                botStateService.unlockState(uid);
            }
            case setting_for_sniper -> {
                userConfigService.updateSniperFee(uid, new BigDecimal(content));
                botSettingHandler.settingKeyboard(bot, uid, null);
                botStateService.unlockState(uid);
            }
            case setting_for_jito_fee -> {
                userConfigService.updateJitoFee(uid, new BigDecimal(content));
                botSettingHandler.settingKeyboard(bot, uid, null);
                botStateService.unlockState(uid);
            }
            // 狙击: 输入token
            case sniper_input_token -> {
                if(!checkService.checkTokenAddressPass(bot, uid, content)){
                    return;
                };
                userActionService.setValue(uid, ActionValEnum.sniper_token_address, content);
                SniperPlan plan = botSniperHandler.sniperCreateSuccess(bot, uid, null);
                botSniperHandler.sniperKeyBoard(bot, uid, null, plan.getId());
                userActionService.setValue(uid, ActionValEnum.sniper_plan_id, plan.getId().toString());
                botStateService.unlockState(uid);
            }
            // 狙击: 输入金额
            case sniper_input_amount -> {
                if(!checkService.isNumberValid(bot, uid, content, 2)){
                    return;
                };
                String planId = userActionService.getValue(uid, ActionValEnum.sniper_plan_id);
                if(sniperPlanWalletRepository.countBind(Long.valueOf(planId)) == 0) {
                    bot.sendMessageToQueue(BotPushMessage.builder()
                            .type(BotMessageTypeEnum.simple_text)
                            .chatId(uid).messageId(null)
                            .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.chooseAtLeastWallet))
                            .build());
                    return;
                }
                BigDecimal amount = new BigDecimal(content);
                sniperGenerateService.finishCreate(uid, Long.valueOf(planId), amount);
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.simple_text)
                        .chatId(uid).messageId(null)
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperCreatedSuccess))
                        .build());
                botSniperHandler.sniperKeyBoard(bot, uid,null, Long.valueOf(planId));
                botStateService.unlockState(uid);
            }
            // 狙击: 设置额外gas
            case sniper_input_extra_gas -> {
                if(!checkService.isNumberValid(bot, uid, content, 3)){
                    return;
                };
                String planId = userActionService.getValue(uid, ActionValEnum.sniper_plan_id);
                BigDecimal amount = new BigDecimal(content);
                sniperGenerateService.setSniperExtraGas(uid, Long.valueOf(planId), amount);
                botSniperHandler.sniperKeyBoard(bot, uid,null, Long.valueOf(planId));
                botStateService.unlockState(uid);
            }
            // 邀请 提现
            case invite_input_wallet -> {
                if(!checkService.isUserAddressValid(bot, uid, content)){
                    return;
                };
                if(!userWithdrawService.withdrawRebate(uid, content)){
                    bot.sendMessageToQueue(BotPushMessage.builder()
                            .type(BotMessageTypeEnum.simple_text)
                            .chatId(uid).messageId(null)
                            .text(CheckError.withdrawRebateFail)
                            .build());
                }
                botStateService.unlockState(uid);
            }
            // 交易-限价单-限价买入
            case deal_to_limit_px_buy -> {
                String[] values = content.replace("，",",").split(",");
                // value0 px; value1; amount
                if(values.length != 2) {
                    return;
                }
                String messageId = userActionService.getValue(uid, ActionValEnum.deal_message_id);
                botDealLimitHandler.tradeLimitPx(bot, uid, Integer.valueOf(messageId), TradeSideEnum.buy, new BigDecimal(values[0]), new BigDecimal(values[1]));
                botStateService.unlockState(uid);
            }
            // 交易-限价单-限价卖出
            case deal_to_limit_px_sell -> {
                String[] values = content.trim().replace("，",",").split(",");
                if(values.length != 2) {
                    return;
                }
                String messageId = userActionService.getValue(uid, ActionValEnum.deal_message_id);
                botDealLimitHandler.tradeLimitPx(bot, uid, Integer.valueOf(messageId), TradeSideEnum.sell,
                        new BigDecimal(values[0]),
                        new BigDecimal(values[1]).movePointLeft(2));
                botStateService.unlockState(uid);
            }
            // 交易-限价单-涨跌幅买入
            case deal_to_limit_rate_buy -> {
                String[] values = content.replace("，",",").split(",");
                if(values.length != 2) {
                    return;
                }
                String messageId = userActionService.getValue(uid, ActionValEnum.deal_message_id);
                botDealLimitHandler.tradeLimitRate(bot, uid, Integer.valueOf(messageId), TradeSideEnum.buy,
                        new BigDecimal(values[0]).movePointLeft(2), new BigDecimal(values[1]));
                botStateService.unlockState(uid);
            }
            // 交易-限价单-涨跌幅卖出
            case deal_to_limit_rate_sell -> {
                String[] values = content.replace("，",",").split(",");
                if(values.length != 2) {
                    return;
                }
                String messageId = userActionService.getValue(uid, ActionValEnum.deal_message_id);
                botDealLimitHandler.tradeLimitRate(bot, uid, Integer.valueOf(messageId), TradeSideEnum.sell,
                        new BigDecimal(values[0]).movePointLeft(2),
                        new BigDecimal(values[1]).movePointLeft(2));
                botStateService.unlockState(uid);
            }
            // 设置-输入挂单配置
            case setting_for_auto_sell -> {
                String[] values = content.replace("，",",").replace("%", "").split(",");
                if(values.length != 2){
                    return;
                }
                botSettingHandler.settingAutoSell(bot, uid, new BigDecimal(values[0]).movePointLeft(2), new BigDecimal(values[1]).movePointLeft(2));
                botStateService.unlockState(uid);
            }
            // 挂刀-输入twitter_name
            case scraper_input_twitter_name -> {
                if(botScraperHandler.dealTwitterName(bot, uid, null, content)){
                    botStateService.unlockState(uid);
                    botStateService.lockState(uid, LockStateEnum.scraper_input_amount);
                } else {
                    botStateService.lockState(uid, LockStateEnum.scraper_input_twitter_name);
                }
            }
            // 挂刀-输入金额
            case scraper_input_amount -> {
                if(!checkService.isNumberValid(bot, uid, content, 3)){
                    return;
                };
                botScraperHandler.generateTask(bot, uid, null, new BigDecimal(content));
                botStateService.unlockState(uid);
            }
            // 挂刀-输入刮刀次数
            case scraper_input_count -> {
                if(!checkService.isNumberValid(bot, uid, content, 0)){
                    return;
                };
                String taskId = userActionService.getValue(uid, ActionValEnum.scraper_keyboard_count_task_id);
                userScraperTaskRepository.updateCount(Long.valueOf(taskId), uid, Long.valueOf(content));
                botScraperHandler.listScraper(bot, uid, null, Long.valueOf(taskId));
                botStateService.unlockState(uid);
            }
            // 挂刀-修改数量
            case scraper_modify_amount -> {
                if(!checkService.isNumberValid(bot, uid, content, 3)){
                    return;
                };
                String taskId = userActionService.getValue(uid, ActionValEnum.scraper_keyboard_amount_task_id);
                userScraperTaskRepository.updateAmount(Long.valueOf(taskId), uid, new BigDecimal(content));
                botScraperHandler.listScraper(bot, uid, null, Long.valueOf(taskId));
                botStateService.unlockState(uid);
            }
        }
    }

    @Resource
    private BotStartHandler botStartHandler;
    @Resource
    private BotWalletHandler botWalletHandler;
    @Resource
    private BotStateService botStateService;
    @Resource
    private BotDealHandler botDealHandler;
    @Resource
    private UserActionService userActionService;
    @Resource
    private UserWalletService userWalletService;
    @Resource
    private UserConfigService userConfigService;
    @Resource
    private BotSniperHandler botSniperHandler;
    @Resource
    private SniperGenerateService sniperGenerateService;
    @Resource
    private UserWithdrawService userWithdrawService;
    @Resource
    private BotSettingHandler botSettingHandler;
    @Resource
    private CheckService checkService;
    @Resource
    private BotPositionHandler botPositionHandler;
    @Resource
    private SniperPlanWalletRepository sniperPlanWalletRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private TwitterSearchService twitterSearchService;

}
