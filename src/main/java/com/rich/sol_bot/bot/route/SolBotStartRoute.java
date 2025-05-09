package com.rich.sol_bot.bot.route;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.handler.*;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyStartConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.wallet.UserWalletService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class SolBotStartRoute {

    @Autowired
    private BotDealLimitHandler botDealLimitHandler;
    @Autowired
    private BotScraperHandler botScraperHandler;
    @Autowired
    private I18nTranslator i18nTranslator;

    public void handlerCallbackQuery(TokenBot bot, Update update) {
        String content = update.getCallbackQuery().getData();
        Long uid = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        switch (content) {
            case BotReplyStartConstants.returnToInitStartCli, BotReplyStartConstants.initStartRefreshCli -> {
                botStartHandler.handlerStart(bot, uid, messageId);
            }
            case BotReplyStartConstants.initStartWalletCli -> {
                if(!userWalletService.walletExist(uid)){
                    botWalletHandler.noWalletStart(bot, uid, messageId);
                } else {
                    botWalletHandler.walletKeyBoard(bot, uid, messageId);
                }
            }
            case BotReplyStartConstants.initStartAutoSniperCli -> {
                if(!userWalletService.walletExist(uid)){
                    botWalletHandler.noWalletStart(bot, uid, messageId);
                } else {
                    botSniperHandler.sniperAddressStart(bot, uid, null);
                    botStateService.lockState(uid, LockStateEnum.sniper_input_token);
                }
            }
            case BotReplyStartConstants.initStartOwnedSniperCli -> {
                botSniperHandler.listSniper(bot, uid, messageId);
            }
            case BotReplyStartConstants.initStartOwnedLimitOrderCli -> {
                botDealLimitHandler.listAllLimitOrders(bot, uid, messageId);
            }
            case BotReplyStartConstants.initStartPositionCli -> {
                botPositionHandler.walletKeyBoard(bot, uid, messageId);
            }
            case BotReplyStartConstants.initStartScraperCli -> {
                botScraperHandler.listScraper(bot, uid, messageId, null);
            }
            case BotReplyStartConstants.initStartDealCli -> {
                if(!userWalletService.walletExist(uid)){
                    botWalletHandler.noWalletStart(bot, uid, null);
                } else {
                    botDealHandler.dealTokenToChoose(bot, uid, null);
                }
            }
            case BotReplyStartConstants.initStartSettingCli -> {
                botSettingHandler.settingKeyboard(bot, uid, messageId);
            }
            case BotReplyStartConstants.initStartInviteCli -> {
                botInviteHandler.inviteKeyboard(bot, uid, messageId);
            }
            case BotReplyStartConstants.initStartHelpCli -> {
                botStartHandler.startHelp(bot, uid);
            }
            case BotReplyStartConstants.initStartLanguageCli -> {
                // 开始设置语言
                botSettingHandler.startSettingLanguage(bot, uid, null);
            }
        }
    }

    /**
     *
     * @param bot
     * @param update
     * @return 是否被响应处理了
     */
    public Boolean handlerReplyContent(TokenBot bot, Update update){
        Long uid = update.getMessage().getFrom().getId();
        String content = update.getMessage().getText();
        boolean handled = true;
        I18nLanguageEnum language = bot.getLanguage(uid);
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.initStartWalletText))){
            if(!userWalletService.walletExist(uid)){
                botWalletHandler.noWalletStart(bot, uid, null);
            } else {
                botWalletHandler.walletKeyBoard(bot, uid, null);
            }
            return true;
        }
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.initStartAutoSniperText))){
            if(!userWalletService.walletExist(uid)){
                botWalletHandler.noWalletStart(bot, uid, null);
            } else {
                botSniperHandler.sniperAddressStart(bot, uid, null);
                botStateService.lockState(uid, LockStateEnum.sniper_input_token);
            }
            return true;
        }
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.initStartOwnedSniperText))){
            botSniperHandler.listSniper(bot, uid, null);
            return true;
        }
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.initStartOwnedLimitOrderText))){
            botDealLimitHandler.listAllLimitOrders(bot, uid, null);
            return true;
        }
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.initStartDealText))){
            if(!userWalletService.walletExist(uid)){
                botWalletHandler.noWalletStart(bot, uid, null);
            } else {
                botDealHandler.dealTokenToChoose(bot, uid, null);
            }
            return true;
        }
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.initStartSettingText))){
            botSettingHandler.settingKeyboard(bot, uid, null);
            return true;
        }
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.initStartInviteText))){
            botPositionHandler.walletKeyBoard(bot, uid, null);
            return true;
        }
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.returnToInitStartText2))){
            botStartHandler.handlerStart(bot, uid, null);
            return true;
        }
        if(content.equals(i18nTranslator.getContent(language, BotReplyAllConstants.initStartPositionText))){
            botPositionHandler.walletKeyBoard(bot, uid, null);
            return true;
        }
        return false;


//
//        switch (content) {
//            // 钱包入口
//            case BotReplyStartConstants.initStartWalletText -> {
//                if(!userWalletService.walletExist(uid)){
//                    botWalletHandler.noWalletStart(bot, uid, null);
//                } else {
//                    botWalletHandler.walletKeyBoard(bot, uid, null);
//                }
//            }
//            // 自动狙击入口
//            case BotReplyStartConstants.initStartAutoSniperText -> {
//                if(!userWalletService.walletExist(uid)){
//                    botWalletHandler.noWalletStart(bot, uid, null);
//                } else {
//                    botSniperHandler.sniperAddressStart(bot, uid, null);
//                    botStateService.lockState(uid, LockStateEnum.sniper_input_token);
//                }
//            }
//            // 已设置狙击入口
//            case BotReplyStartConstants.initStartOwnedSniperText -> {
//                botSniperHandler.listSniper(bot, uid, null);
//            }
//            // 已设置挂单入口
//            case BotReplyStartConstants.initStartOwnedLimitOrderText -> {
//                botDealLimitHandler.listAllLimitOrders(bot, uid, null);
//            }
//            // 买/卖 入口
//            case BotReplyStartConstants.initStartDealText -> {
//                if(!userWalletService.walletExist(uid)){
//                    botWalletHandler.noWalletStart(bot, uid, null);
//                } else {
//                    botDealHandler.dealTokenToChoose(bot, uid, null);
//                }
//            }
//            // 开始设置入口
//            case BotReplyStartConstants.initStartSettingText -> {
//                botSettingHandler.settingKeyboard(bot, uid, null);
//            }
//            // 要请入口
//            case BotReplyStartConstants.initStartInviteText -> {
//                botInviteHandler.inviteKeyboard(bot, uid, null);
//            }
//            // 持仓入口
//            case BotReplyStartConstants.initStartPositionText -> {
//                botPositionHandler.walletKeyBoard(bot, uid, null);
//            }
//            // 主菜单入口
//            case BotReplyStartConstants.returnToInitStartText2 -> {
//                botStartHandler.handlerStart(bot, uid, null);
//            }
//            default -> {
//                handled = false;
//            }
//        }
//        return handled;
    }

    @Resource
    private BotStartHandler botStartHandler;
    @Resource
    private BotWalletHandler botWalletHandler;
    @Resource
    private BotDealHandler botDealHandler;
    @Resource
    private UserWalletService userWalletService;
    @Resource
    private BotSettingHandler botSettingHandler;
    @Resource
    private BotSniperHandler botSniperHandler;
    @Resource
    private BotInviteHandler botInviteHandler;
    @Resource
    private BotStateService botStateService;
    @Resource
    private BotPositionHandler botPositionHandler;

}
