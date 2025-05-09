package com.rich.sol_bot.bot.handler;

import com.rich.sol_bot.admin.stat.StatService;
import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplySettingConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyStartConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.user.config.UserConfigService;
import com.rich.sol_bot.user.config.mapper.ConfigAutoSell;
import com.rich.sol_bot.user.config.mapper.ConfigAutoSellRepository;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.User;
import com.rich.sol_bot.user.mapper.UserRepository;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BotSettingHandler {
    @Autowired
    private ConfigAutoSellRepository configAutoSellRepository;
    @Autowired
    private StatService statService;
    @Autowired
    private BotStateService botStateService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private I18nTranslator i18nTranslator;
    @Autowired
    private UserRepository userRepository;

    public void settingKeyboard(TokenBot bot, Long uid, Integer messageId) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        UserConfig userConfig = userConfigRepository.getById(uid);
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingFastSlippageText))
                                .callbackData(BotReplySettingConstants.settingFastSlippageCli).build(),
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingProtectSlippageText))
                        .callbackData(BotReplySettingConstants.settingProtectSlippageCli).build()
                )
        );
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingBuyGasText))
                                .callbackData(BotReplySettingConstants.settingBuyGasCli).build(),
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingSellGasText))
                                .callbackData(BotReplySettingConstants.settingSellGasCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingSniperGasText))
                        .callbackData(BotReplySettingConstants.settingSniperGasCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingPreferWalletText))
                        .callbackData(BotReplySettingConstants.settingPreferWalletCli).build()
        ));
        keyboard.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.isModeOn(userConfig.getAutoSell() == 1)))
                .callbackData(BotReplySettingConstants.settingAutoSellCli)
                .build())
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                                .callbackData(BotReplyStartConstants.returnToInitStartCli).build()
                )
        );
        markup.setKeyboard(keyboard);
//        UserConfig userConfig = userConfigRepository.getById(uid);
        String walletName = "";
        UserWallet preferWallet = userWalletRepository.getPreferWallet(uid, userConfig.getPreferWallet());
        if(preferWallet != null){
            walletName = preferWallet.getName();
        }
        String content = String.format(
                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingStart),
                userConfig.showFastSlippage(), userConfig.showProtectSlippage(),
                userConfig.showBuyFee(), userConfig.showSellFee(), userConfig.showSniperFee(),
                walletName
        );
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(content).inlineKeyboardMarkup(markup)
                .build());
    }

    public void handlerToSetting(TokenBot bot, Long uid, LockStateEnum state, Integer messageId) {
        UserConfig userConfig = userConfigRepository.getById(uid);
        String content = "";
        switch (state) {
            case setting_for_fast -> {
                content = i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingForFast);
            }
            case setting_for_protect -> {
                content = i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingForProtect);
            }
            case setting_for_buy_gas -> {
                content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingForBuyGas), userConfig.showBuyFee());
            }
            case setting_for_sell_gas -> {
                content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingForSellGas), userConfig.showSellFee());
            }
            case setting_for_sniper -> {
                content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingForSniper), userConfig.showSniperFee());
            }
            case setting_for_jito_fee -> {
                content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingForJito), userConfig.showJitoFee());
            }
        }
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(messageId)
                .text(content)
                .build());
    }

    public void showPreferWallet(TokenBot bot, Long uid, Integer messageId) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        UserConfig userConfig = userConfigRepository.getById(uid);
        List<UserWallet> wallets = userWalletRepository.listByUid(uid);
        for(UserWallet wallet: wallets) {
            keyboard.add(Collections.singletonList(
                            InlineKeyboardButton.builder().text("钱包: " + wallet.getName() + BotReplySettingConstants.showButton(Objects.equals(userConfig.getPreferWallet(), wallet.getId())))
                                    .callbackData(BotReplySettingConstants.settingPreferWalletPrefix + wallet.getId()).build()
                    )
            );
        }
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingPreferWalletReturnText))
                                .callbackData(BotReplySettingConstants.settingPreferWalletReturnCli).build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingPreferWalletHead)).inlineKeyboardMarkup(markup)
                .build());
    }

    // 进入自动卖出设置面板
    public void showSettingAutoSell(TokenBot bot, Long uid, Integer messageId) {
        UserConfig userConfig = userConfigRepository.getById(uid);
        List<ConfigAutoSell> list = configAutoSellRepository.listExist(uid);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

        StringBuilder content = new StringBuilder(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingAutoSellContent));
        if(list.isEmpty()) {
            content.append(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealAutoSellNoConfig));
        } else {
            content.append(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealAutoSellConfigTitle));
            int count = 0;
            for(ConfigAutoSell item: list) {
                count += 1;
                content.append(String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealAutoSellConfigContent),
                        count,
                        NumberFormatTool.formatNumber(item.getPxRate().movePointRight(2),4) + "%",
                        NumberFormatTool.formatNumber(item.getTokenPercent().movePointRight(2), 4) + "%"));
                keyboardButtons.add(InlineKeyboardButton.builder().text(
                        i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealAutoSellConfigCancel
                        ) + count)
                        .callbackData(BotReplySettingConstants.settingAutoSellCancelCliPrefix + item.getId()).build());
                if(keyboardButtons.size() == 3) {
                    keyboard.add(keyboardButtons);
                    keyboardButtons = new ArrayList<>();
                }
            }
        }
        keyboard.add(keyboardButtons);
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(
                                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.isModeOn(userConfig.getAutoSell() == 1)))
                        .callbackData(BotReplySettingConstants.settingAutoSellSwitchCli).build())
        );
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingAutoSellConfigText))
                        .callbackData(BotReplySettingConstants.settingAutoSellConfigCli).build())
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(content.toString()).inlineKeyboardMarkup(markup)
                .build());
    }

    // 开始设置价格涨跌幅和买卖比例
    public void startSettingAutoSell(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(messageId)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.settingAutoSellConfig))
                .build());
        botStateService.lockState(uid, LockStateEnum.setting_for_auto_sell);
    }

    // 处理输入的价格涨跌幅和买卖比例
    public void settingAutoSell(TokenBot bot, Long uid, BigDecimal pxRate, BigDecimal tokenPercent) {
        configAutoSellRepository.save(ConfigAutoSell.builder()
                        .id(IdUtil.nextId()).uid(uid).pxRate(pxRate)
                        .createdAt(TimestampUtil.now())
                        .tokenPercent(tokenPercent).deleted(0)
                .build());
        this.showSettingAutoSell(bot, uid, null);
    }

    // 取消自动售出的配置
    public void deleteAutoSellConfig(TokenBot bot, Long uid, Integer messageId, Long configId) {
        configAutoSellRepository.deleteAutoConfig(configId);
        this.showSettingAutoSell(bot, uid, messageId);
    }

    // 开启关闭自动卖出
    public void switchAutoSell(TokenBot bot, Long uid, Integer messageId) {
        userConfigService.switchAutoSell(uid);
        this.showSettingAutoSell(bot, uid, messageId);
    }

    // 开始设置语言
    public void startSettingLanguage(TokenBot bot, Long uid, Integer messageId) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        User user = userRepository.getById(uid);
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder().text("简体中文" + BotReplySettingConstants.showButton(user.getLanguage().equals(I18nLanguageEnum.zh_CN)))
                                .callbackData(BotReplySettingConstants.settingLanguage + I18nLanguageEnum.zh_CN).build(),
                        InlineKeyboardButton.builder().text("English" + BotReplySettingConstants.showButton(user.getLanguage().equals(I18nLanguageEnum.en_US)))
                                .callbackData(BotReplySettingConstants.settingLanguage + I18nLanguageEnum.en_US).build()
                )
        );
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder().text("한국어" + BotReplySettingConstants.showButton(user.getLanguage().equals(I18nLanguageEnum.ko)))
                                .callbackData(BotReplySettingConstants.settingLanguage + I18nLanguageEnum.ko).build(),
                        InlineKeyboardButton.builder().text("Tiếng Việt"+ BotReplySettingConstants.showButton(user.getLanguage().equals(I18nLanguageEnum.vi)))
                                .callbackData(BotReplySettingConstants.settingLanguage + I18nLanguageEnum.vi).build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartLanguageText)).inlineKeyboardMarkup(markup)
                .build());
    }

    @Resource
    private UserConfigRepository userConfigRepository;
    @Resource
    private UserWalletRepository userWalletRepository;
}
