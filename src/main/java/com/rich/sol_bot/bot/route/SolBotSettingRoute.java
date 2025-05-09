package com.rich.sol_bot.bot.route;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.handler.BotSettingHandler;
import com.rich.sol_bot.bot.handler.BotStartHandler;
import com.rich.sol_bot.bot.handler.BotStateService;
import com.rich.sol_bot.bot.handler.constants.BotReplySettingConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.user.config.UserConfigService;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class SolBotSettingRoute {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BotStartHandler botStartHandler;

    public void handlerCallbackQuery(TokenBot bot, Update update) {
        String content = update.getCallbackQuery().getData();
        Long uid = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        if(content.startsWith(BotReplySettingConstants.settingPreferWalletPrefix)){
            String walletId = content.replace(BotReplySettingConstants.settingPreferWalletPrefix, "");
            userConfigService.updatePreferWallet(uid, Long.valueOf(walletId));
            botSettingHandler.showPreferWallet(bot, uid, messageId);
        }
        if(content.startsWith(BotReplySettingConstants.settingAutoSellCancelCliPrefix)) {
            String configId = content.replace(BotReplySettingConstants.settingAutoSellCancelCliPrefix, "");
            botSettingHandler.deleteAutoSellConfig(bot, uid, messageId, Long.valueOf(configId));
        }
        if(content.startsWith(BotReplySettingConstants.settingLanguage)){
            String lan = content.replace(BotReplySettingConstants.settingLanguage, "");
            I18nLanguageEnum language = I18nLanguageEnum.valueOf(lan);
            userRepository.setLanguage(uid, language);
            bot.setLanguage(uid, language);
            botSettingHandler.startSettingLanguage(bot, uid, messageId);
            botStartHandler.handlerStartReplyKeyBoard(bot, uid, null);
            botStartHandler.handlerStart(bot, uid, null);
        }

        switch (content) {
            // 设置极速模式
            case BotReplySettingConstants.settingFastSlippageCli -> {
                botSettingHandler.handlerToSetting(bot, uid, LockStateEnum.setting_for_fast, null);
                botStateService.lockState(uid, LockStateEnum.setting_for_fast);
            }
            // 设置防夹模式
            case BotReplySettingConstants.settingProtectSlippageCli -> {
                botSettingHandler.handlerToSetting(bot, uid, LockStateEnum.setting_for_protect, null);
                botStateService.lockState(uid, LockStateEnum.setting_for_protect);
            }
            // 设置买入gas
            case BotReplySettingConstants.settingBuyGasCli -> {
                botSettingHandler.handlerToSetting(bot, uid, LockStateEnum.setting_for_buy_gas, null);
                botStateService.lockState(uid, LockStateEnum.setting_for_buy_gas);
            }
            // 设置卖出gas
            case BotReplySettingConstants.settingSellGasCli -> {
                botSettingHandler.handlerToSetting(bot, uid, LockStateEnum.setting_for_sell_gas, null);
                botStateService.lockState(uid, LockStateEnum.setting_for_sell_gas);
            }
            // 设置狙击gas
            case BotReplySettingConstants.settingSniperGasCli -> {
                botSettingHandler.handlerToSetting(bot, uid, LockStateEnum.setting_for_sniper, null);
                botStateService.lockState(uid, LockStateEnum.setting_for_sniper);
            }
            // 设置防夹模式优先费
            case BotReplySettingConstants.settingJitoGasCli -> {
                botSettingHandler.handlerToSetting(bot, uid, LockStateEnum.setting_for_jito_fee, null);
                botStateService.lockState(uid, LockStateEnum.setting_for_jito_fee);
            }
            // 设置默认钱包
            case BotReplySettingConstants.settingPreferWalletCli -> {
                botSettingHandler.showPreferWallet(bot, uid, messageId);
            }
            // 返回设置页面
            case BotReplySettingConstants.settingPreferWalletReturnCli -> {
                botSettingHandler.settingKeyboard(bot, uid, messageId);
            }
            // 设置自动卖出
            case BotReplySettingConstants.settingAutoSellCli -> {
                botSettingHandler.showSettingAutoSell(bot, uid, null);
            }
            // 开关是否自动卖出
            case BotReplySettingConstants.settingAutoSellSwitchCli -> {
                botSettingHandler.switchAutoSell(bot, uid, messageId);
            }
            // 新增自动卖出设置
            case BotReplySettingConstants.settingAutoSellConfigCli -> {
                botSettingHandler.startSettingAutoSell(bot, uid, messageId);
            }
        }
    }

    @Resource
    private BotSettingHandler botSettingHandler;
    @Resource
    private BotStateService botStateService;
    @Resource
    private UserConfigService userConfigService;
}
