package com.rich.sol_bot.bot.route;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.handler.BotSettingHandler;
import com.rich.sol_bot.bot.handler.BotSniperHandler;
import com.rich.sol_bot.bot.handler.BotStartHandler;
import com.rich.sol_bot.bot.handler.BotStateService;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyInviteConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplySniperConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.sniper.SniperGenerateService;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.sniper.SniperPlanWalletService;
import com.rich.sol_bot.sniper.mapper.SniperPlanRepository;
import com.rich.sol_bot.sniper.mapper.SniperPlanWalletRepository;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;


@Service
public class SolBotSniperRoute {

    @Autowired
    private I18nTranslator i18nTranslator;

    public void handlerCallbackQuery(TokenBot bot, Update update) {
        String content = update.getCallbackQuery().getData();
        Long uid = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        String callBackDataId = update.getCallbackQuery().getId();

        // 钱包切换
        if(content.startsWith(BotReplySniperConstants.sniperWalletBindPrefix)){
            String planIdAndWallet = content.replace(BotReplySniperConstants.sniperWalletBindPrefix, "");
            String[] stringList = planIdAndWallet.split(":");
            if(stringList.length == 2) {
                String planId = stringList[0];
                String walletId = stringList[1];
                sniperGenerateService.bindWalletSwitch(uid, Long.valueOf(planId), Long.valueOf(walletId));
                botSniperHandler.sniperKeyBoard(bot, uid, messageId, Long.valueOf(planId));
            }
        }
        // 狙击列表
        if(content.startsWith(BotReplySniperConstants.sniperListNameCliPrefix)){
            String planId = content.replace(BotReplySniperConstants.sniperListNameCliPrefix, "");
            userActionService.setValue(uid, ActionValEnum.sniper_plan_id, planId);
            botSniperHandler.sniperKeyBoard(bot, uid, messageId, Long.valueOf(planId));
        }
        // 买入数量
        if(content.startsWith(BotReplySniperConstants.buyPrefix)){
            String planIdAndAmount = content.replace(BotReplySniperConstants.buyPrefix, "");
            String[] stringList = planIdAndAmount.split(":");
            if(stringList.length == 2) {
                Long planId = Long.valueOf(stringList[0]);
                if(sniperPlanWalletRepository.countBind(planId) == 0) {
                    bot.sendMessageToQueue(BotPushMessage.builder()
                            .type(BotMessageTypeEnum.simple_text)
                            .chatId(uid).messageId(null)
                            .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.chooseAtLeastWallet))
                            .build());
                    return;
                }
                if(BotReplySniperConstants.unKnowAmount.equals(stringList[1])){
                    botSniperHandler.sniperInputAmount(bot, uid, messageId);
                    botStateService.lockState(uid, LockStateEnum.sniper_input_amount);
                    userActionService.setValue(uid, ActionValEnum.sniper_plan_id, planId.toString());
                } else {
                    BigDecimal amount = new BigDecimal(stringList[1]);
                    sniperGenerateService.finishCreate(uid, planId, amount);
                    bot.sendMessageToQueue(BotPushMessage.builder()
                            .type(BotMessageTypeEnum.simple_text)
                            .chatId(uid).messageId(null)
                            .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.sniperCreatedSuccess))
                            .build());
                    botSniperHandler.sniperKeyBoard(bot, uid, messageId, planId);
                }
            }
            return;
        }
        // 删除
        if(content.startsWith(BotReplySniperConstants.sniperDeleteCli)){
            String planId = content.replace(BotReplySniperConstants.sniperDeleteCli, "");
            sniperGenerateService.deleteSnipPlan(Long.valueOf(planId));
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.answer_call_back_data)
                    .callbackDataId(callBackDataId)
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.deleteSnapPlanSuccess))
                    .build());
            botStartHandler.handlerStart(bot, uid, messageId);
        }
        switch (content) {
            // 刷新面板
            case BotReplySniperConstants.sniperKeyboardRefreshCli -> {
                botSniperHandler.sniperKeyBoard(bot, uid, messageId);
            }
            // 设置贿赂手续费
            case BotReplySniperConstants.sniperExtraGasCli -> {
                botStateService.lockState(uid, LockStateEnum.sniper_input_extra_gas);
                botSniperHandler.setSniperExtraGas(bot, uid, messageId);
            }
            // 跳到设置
            case BotReplySniperConstants.sniperNewSettingCli -> {
                botSettingHandler.settingKeyboard(bot, uid, null);
            }
            // 设置成快速模式
            case BotReplySniperConstants.sniperSlippageFastModeCli -> {
                String planId = userActionService.getValue(uid, ActionValEnum.sniper_plan_id);
                sniperGenerateService.setSniperMode(uid, Long.valueOf(planId), SniperMode.fast_mode);
                botSniperHandler.sniperKeyBoard(bot, uid, messageId, Long.valueOf(planId));
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.answer_call_back_data)
                        .callbackDataId(callBackDataId)
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.setFastModeSuccess))
                        .build());
            }
            // 设置成保护模式
            case BotReplySniperConstants.sniperSlippageProtectModeCli -> {
                String planId = userActionService.getValue(uid, ActionValEnum.sniper_plan_id);
                sniperGenerateService.setSniperMode(uid, Long.valueOf(planId), SniperMode.protect_mode);
                botSniperHandler.sniperKeyBoard(bot, uid, messageId, Long.valueOf(planId));
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.answer_call_back_data)
                        .callbackDataId(callBackDataId)
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.setProtectModeSuccess))
                        .build());
            }
        }
    }

    @Resource
    private BotSniperHandler botSniperHandler;
    @Resource
    private SniperPlanWalletRepository sniperPlanWalletRepository;
    @Resource
    private UserActionService userActionService;
    @Resource
    private BotStartHandler botStartHandler;
    @Resource
    private SniperGenerateService sniperGenerateService;
    @Resource
    private BotStateService botStateService;
    @Resource
    private BotSettingHandler botSettingHandler;

}
