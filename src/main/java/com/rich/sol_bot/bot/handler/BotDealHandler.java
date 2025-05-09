package com.rich.sol_bot.bot.handler;

import com.rich.sol_bot.bot.BotManager;
import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.CheckError;
import com.rich.sol_bot.bot.handler.constants.*;
import com.rich.sol_bot.bot.handler.enums.KeyBoardType;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.iceberg.IcebergService;
import com.rich.sol_bot.iceberg.mapper.IcebergTask;
import com.rich.sol_bot.system.enums.LanguageEnum;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.mapper.TradeInfoRepository;
import com.rich.sol_bot.trade.service.TokenBalanceService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.trade.service.TradeService;
import com.rich.sol_bot.trade.enums.TradeSideEnum;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.UserRepository;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStatRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Slf4j
public class BotDealHandler {

    @Value("${bot.deal.confirm.limit:5}")
    public String confirmLimit;
    @Autowired
    private TradeInfoRepository tradeInfoRepository;
    @Resource
    private I18nTranslator i18nTranslator;
    @Autowired
    private UserRepository userRepository;

    public void pushConfirm(TokenBot bot, Long uid, Integer messageId, BigDecimal solAmount, BigDecimal amount, String side, SniperMode mode) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealConfirmSuccessText))
                .callbackData(BotReplyDealConstants.dealConfirmSuccessCli + messageId + ":" + amount.stripTrailingZeros().toPlainString() + ":" + side)
                .build()));
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealConfirmCancelText))
                                .callbackData(BotReplyDealConstants.dealConfirmCancelCli)
                                .build()
                )
        );
        markup.setKeyboard(keyboard);
        String content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealToConfirmTwice), solAmount, mode.getZhValue());
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(null).inlineKeyboardMarkup(markup)
                .text(content)
                .build());
    }


    public void dealTokenToChoose(TokenBot bot, Long uid, Integer messageId) {
//        bot.pushContent(uid, null, BotReplyDealConstants.dealTokenToChoose);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealTokenToChoose))
                .build());
        botStateService.lockState(uid, LockStateEnum.deal_to_choose_token);
    }

    public void showKeyBoard(Long uid, Integer messageId, Long tokenId, Long walletId, Long page, String modeString, Boolean dealPin, KeyBoardType type) {
        TokenBaseInfo tokenInfo = null;
        UserWallet userWallet = null;
        if(page == null) {
            String pageStr = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_page);
            if(pageStr != null){
                page = Long.valueOf(pageStr);
            } else {
                page = 1L;
            }
        }
        if(tokenId == null) {
            String tokenAddress = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
            tokenInfo = tokenInfoService.geneTokenBaseInfo(tokenAddress);
        } else {
            tokenInfo = tokenBaseInfoRepository.getById(tokenId);
        }
        if(walletId == null) {
            walletId = Long.valueOf(userActionService.getValue(uid, messageId, ActionValEnum.deal_wallet_id));
        }
        userWallet = userWalletRepository.getById(walletId);
        if(modeString == null) {
            modeString = userActionService.getValue(uid, messageId, ActionValEnum.deal_mode);
        }
        if(modeString == null && tradeInfoRepository.isProtect(uid, tokenInfo.getId())){
            modeString = SniperMode.protect_mode.toString();
        }
        if(modeString == null) {
            modeString = SniperMode.protect_mode.toString();
        }
        SniperMode mode = SniperMode.valueOf(modeString);
        if(dealPin == null) {
            String dealPinStr = userActionService.getValue(uid, messageId, ActionValEnum.deal_message_pin);
            if(dealPinStr != null) {
                dealPin = Boolean.valueOf(dealPinStr);
            } else {
                dealPin = false;
            }
        }
        EditMessageText data = null;
        switch (type) {
            case common -> {
                data = this.generateKeyBoard(uid, messageId, tokenInfo, userWallet, mode, page, dealPin);
            }
            case ice -> {
                data = this.generateKeyIceBoard(uid, messageId, tokenInfo, userWallet, mode, page, dealPin);
            }
            case limit -> {
                data = this.generateLimitKeyBoard(uid, messageId, tokenInfo, userWallet, mode, page, dealPin);
            }
        }
        if(messageId == null) {
            Boolean finalDealPin = dealPin;
            TokenBaseInfo finalTokenInfo = tokenInfo;
            Long finalWalletId = walletId;
            Long finalPage = page;
            botManager.pushContent(uid, null, data.getText(), data.getReplyMarkup(), mid -> {
                if(finalDealPin) {
                    botManager.unpinAll(uid, msgId -> {
                        botManager.pinMsg(uid, mid, null);
                    });
                }
                userActionService.setValue(uid, mid, ActionValEnum.deal_token_address, finalTokenInfo.getAddress());
                userActionService.setValue(uid, mid, ActionValEnum.deal_mode, mode.getValue());
                userActionService.setValue(uid, mid, ActionValEnum.deal_wallet_id, finalWalletId.toString());
                userActionService.setValue(uid, mid, ActionValEnum.deal_token_page, finalPage.toString());
                userActionService.setValue(uid, mid, ActionValEnum.deal_message_pin, finalDealPin.toString());
            });
        } else {
            botManager.pushContent(uid, messageId, data.getText(), data.getReplyMarkup(), null);
        }
    }


    public EditMessageText generateKeyBoard(Long uid, Integer messageId,
                                            TokenBaseInfo tokenBaseInfo, UserWallet nowWallet, SniperMode mode,
                                            Long page,
                                            Boolean showTokens
    ) {
        I18nLanguageEnum language = userRepository.getLanguage(uid);
        TokenBaseInfo tokenMain = tokenInfoService.getMain();
        UserConfig userConfig = userConfigRepository.getById(uid);
        List<UserWallet> wallets = userWalletRepository.listByUid(uid);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
//                                .text(BotReplyDealConstants.dealPinRefreshText)
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealPinRefreshText))
                                .callbackData(BotReplyDealConstants.dealPinRefreshCli)
                                .build()
                )
        );
        List<InlineKeyboardButton> walletButtons = new ArrayList<>();
        for (UserWallet wallet : wallets) {
            walletButtons.add(
                    InlineKeyboardButton.builder()
                            .text(BotReplySniperConstants.sniperBindButton(nowWallet.getId().equals(wallet.getId())) + wallet.getName())
                            .callbackData(BotReplyDealConstants.dealWalletPrefix + wallet.getId())
                            .build()
            );
            if (walletButtons.size() == 3) {
                keyboard.add(walletButtons);
                walletButtons = new ArrayList<>();
            }
        }
        if (!walletButtons.isEmpty()) {
            keyboard.add(walletButtons);
        }
        if(showTokens) {
            List<WalletBalanceStat> walletBalanceStats = walletBalanceStatRepository.listExistByUidAndWalletId(uid, nowWallet.getId());
            walletBalanceStats = walletBalanceStats.stream().filter(o -> !o.getTokenId().equals(tokenMain.getId())).toList();
            Integer pageTotal = walletBalanceStats.size() / 6;
            if(walletBalanceStats.size() % 6 > 0) {
                pageTotal += 1;
            }
            if(page <= 0) {
                page = 1L;
                userActionService.setValue(uid, messageId, ActionValEnum.deal_token_page, page.toString());
            } else if(page > pageTotal) {
                page = pageTotal.longValue();
                userActionService.setValue(uid, messageId, ActionValEnum.deal_token_page, page.toString());
            }

            List<TokenBaseInfo> tokenBaseInfos = tokenBaseInfoRepository.listByIds(walletBalanceStats.stream().map(WalletBalanceStat::getTokenId).toList());
            Map<Long, String> map = new HashMap<>();
            tokenBaseInfos.forEach(o -> {
                map.put(o.getId(), o.getSymbol());
            });
            List<InlineKeyboardButton> tokenButtons = new ArrayList<>();
            Long allCount = 0L;
            if(page == 0) page = 1L;
            for(WalletBalanceStat stat: walletBalanceStats.stream().skip((page - 1) * 6).limit(6).toList()) {
                String name = map.get(stat.getTokenId());
                tokenButtons.add(
                        InlineKeyboardButton.builder()
                                .text(BotReplySniperConstants.sniperBindButton(stat.getTokenId().equals(tokenBaseInfo.getId())) + name)
                                .callbackData(BotReplyDealConstants.dealTokenPrefix + stat.getTokenId())
                                .build()
                );
                allCount += 1;
                if (tokenButtons.size() == 3) {
                    keyboard.add(tokenButtons);
                    tokenButtons = new ArrayList<>();
                }
                if(allCount == 6){
                    break;
                }
            }
            if (!tokenButtons.isEmpty()) {
                keyboard.add(tokenButtons);
            }
            keyboard.add(Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealTokenPagePreText))
                                    .callbackData(BotReplyDealConstants.dealTokenPagePreCli)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(String.format("(%s/%s)", page, pageTotal))
                                    .callbackData(" ")
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealTokenPageNexText))
                                    .callbackData(BotReplyDealConstants.dealTokenPageNexCli)
                                    .build()
                    )
            );
        }
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(BotReplyDealConstants.isModeOn(mode.equals(SniperMode.fast_mode)) +
                                        i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealFastModeText)
                                )
                                .callbackData(BotReplyDealConstants.dealFastModeCli)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(BotReplyDealConstants.isModeOn(mode.equals(SniperMode.protect_mode)) +
                                        i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealProtectModeText))
                                .callbackData(BotReplyDealConstants.dealProtectModeCli)
                                .build()
                )
        );
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealToIcebergText))
                        .callbackData(BotReplyDealConstants.dealToIcebergCli)
                        .build()
        ));
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("-- " +i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealBuyTextPrefix)+ " --")
                        .callbackData("-- 买 --")
                        .build()
        ));
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(
                                        i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealBuyTextPrefix) +
                                                BotReplyDealConstants.generateDealBuyText(BotReplyDealConstants.dealBuyAmountList.get(0))
                                )
                                .callbackData(BotReplyDealConstants.generateDealBuyCli(BotReplyDealConstants.dealBuyAmountList.get(0)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealBuyTextPrefix) + BotReplyDealConstants.generateDealBuyText(BotReplyDealConstants.dealBuyAmountList.get(1)))
                                .callbackData(BotReplyDealConstants.generateDealBuyCli(BotReplyDealConstants.dealBuyAmountList.get(1)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealBuyTextPrefix) + BotReplyDealConstants.generateDealBuyText(BotReplyDealConstants.dealBuyAmountList.get(2)))
                                .callbackData(BotReplyDealConstants.generateDealBuyCli(BotReplyDealConstants.dealBuyAmountList.get(2)))
                                .build()
                )
        );
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealBuyTextPrefix) + BotReplyDealConstants.generateDealBuyText(BotReplyDealConstants.dealBuyAmountList.get(3)))
                                .callbackData(BotReplyDealConstants.generateDealBuyCli(BotReplyDealConstants.dealBuyAmountList.get(3)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealBuyTextPrefix) + BotReplyDealConstants.generateDealBuyText(BotReplyDealConstants.dealBuyAmountList.get(4)))
                                .callbackData(BotReplyDealConstants.generateDealBuyCli(BotReplyDealConstants.dealBuyAmountList.get(4)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealBuyTextPrefix) + BotReplyDealConstants.generateDealBuyText(BotReplyDealConstants.dealBuyAmountList.get(5)))
                                .callbackData(BotReplyDealConstants.generateDealBuyCli(BotReplyDealConstants.dealBuyAmountList.get(5)))
                                .build()
                )
        );
        keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("-- " + i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealSellTextPrefix) + " --")
                        .callbackData("-- 卖 --")
                        .build()
        ));
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealSellTextPrefix) + BotReplyDealConstants.generateDealSellText(BotReplyDealConstants.dealSellAmountList.get(0)))
                                .callbackData(BotReplyDealConstants.generateDealSellCli(BotReplyDealConstants.dealSellAmountValueList.get(0)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealSellTextPrefix) + BotReplyDealConstants.generateDealSellText(BotReplyDealConstants.dealSellAmountList.get(1)))
                                .callbackData(BotReplyDealConstants.generateDealSellCli(BotReplyDealConstants.dealSellAmountValueList.get(1)))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealSellTextPrefix) + BotReplyDealConstants.generateDealSellText(BotReplyDealConstants.dealSellAmountList.get(2)))
                                .callbackData(BotReplyDealConstants.generateDealSellCli(BotReplyDealConstants.dealSellAmountValueList.get(2)))
                                .build()
                )
        );
        keyboard.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitText))
                        .callbackData(BotReplyDealConstants.dealLimitCli)
                        .build(),
                InlineKeyboardButton.builder()
                        .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.isModeOn(userConfig.getAutoSell() == 1)))
                        .callbackData(BotReplySettingConstants.settingAutoSellCli)
                        .build(),
                InlineKeyboardButton.builder()
                        .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealToShowPLText))
                        .callbackData(BotReplyDealConstants.dealToShowPLCli)
                        .build()
        ));
        if(!showTokens) {
            keyboard.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.returnToInitStartText))
                            .callbackData(BotReplyStartConstants.returnToInitStartCli)
                            .build()
            ));
        }
        markup.setKeyboard(keyboard);
        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealKeyboardHead),
                tokenBaseInfo.getSymbol(),
                tokenBaseInfo.getAddress()
        );
        content += botReplyGenerateService.generateTokenStaticHead(tokenBaseInfo, language);
//            content += botReplyGenerateService.generateTokenGroup(tokenBaseInfo);
        content += botReplyGenerateService.generateTokenStaticSocial(tokenBaseInfo, language);
        content += botReplyGenerateService.generateWalletStatic2(uid, tokenBaseInfo, nowWallet);
        return EditMessageText.builder().text(content).chatId(uid).replyMarkup(markup).build();
    }


    public EditMessageText generateKeyIceBoard(Long uid, Integer messageId,
                                            TokenBaseInfo tokenBaseInfo, UserWallet nowWallet, SniperMode mode,
                                            Long page,
                                            Boolean showTokens
    ) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        I18nLanguageEnum language = userRepository.getLanguage(uid);
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealToIcebergBuyText))
                                .callbackData(BotReplyDealConstants.dealToIcebergBuyCli)
                                .build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealToIcebergSellText))
                                .callbackData(BotReplyDealConstants.dealToIcebergSellCli)
                                .build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealToIcebergReturnText))
                                .callbackData(BotReplyDealConstants.dealToIcebergReturnCli)
                                .build()
                )
        );
        markup.setKeyboard(keyboard);
        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealKeyboardHead),
                tokenBaseInfo.getSymbol(),
                tokenBaseInfo.getAddress()
        );
        content += botReplyGenerateService.generateTokenStaticHead(tokenBaseInfo, language);
//            content += botReplyGenerateService.generateTokenGroup(tokenBaseInfo);
        content += botReplyGenerateService.generateTokenStaticSocial(tokenBaseInfo, language);
        content += botReplyGenerateService.generateWalletStatic2(uid, tokenBaseInfo, nowWallet);
        return EditMessageText.builder().text(content).chatId(uid).replyMarkup(markup).build();
    }

    public EditMessageText generateLimitKeyBoard(Long uid, Integer messageId,
                                               TokenBaseInfo tokenBaseInfo, UserWallet nowWallet, SniperMode mode,
                                               Long page,
                                               Boolean showTokens
    ) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        I18nLanguageEnum language = userRepository.getLanguage(uid);
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(BotReplyDealConstants.isModeOn(mode.equals(SniperMode.fast_mode)) +
                                        i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitFastModeText))
                                .callbackData(BotReplyDealConstants.dealLimitFastModeCli)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(BotReplyDealConstants.isModeOn(mode.equals(SniperMode.protect_mode)) +
                                        i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitProtectModeText))
                                .callbackData(BotReplyDealConstants.dealLimitProtectModeCli)
                                .build()
                )
        );
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitPxBuyText))
                                .callbackData(BotReplyDealConstants.dealLimitPxBuyCli)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitPxSellText))
                                .callbackData(BotReplyDealConstants.dealLimitPxSellCli)
                                .build()
                )
        );
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitRateBuyText))
                                .callbackData(BotReplyDealConstants.dealLimitRateBuyCli)
                                .build(),
                InlineKeyboardButton.builder()
                        .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitRateSellText))
                        .callbackData(BotReplyDealConstants.dealLimitRateSellCli)
                        .build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitListOrdersText))
                                .callbackData(BotReplyDealConstants.dealLimitListOrdersCli)
                                .build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealLimitReturnText))
                                .callbackData(BotReplyDealConstants.dealLimitReturnCli)
                                .build()
                )
        );
        markup.setKeyboard(keyboard);
        String content = String.format(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.dealKeyboardHead),
                tokenBaseInfo.getSymbol(),
                tokenBaseInfo.getAddress()
        );
        content += botReplyGenerateService.generateTokenStaticHead(tokenBaseInfo, language);
//            content += botReplyGenerateService.generateTokenGroup(tokenBaseInfo);
        content += botReplyGenerateService.generateTokenStaticSocial(tokenBaseInfo, language);
        content += botReplyGenerateService.generateWalletStatic2(uid, tokenBaseInfo, nowWallet);
        return EditMessageText.builder().text(content).chatId(uid).replyMarkup(markup).build();
    }

    public void dealImportAmount(TokenBot bot, Long uid, Integer messageId, BigDecimal amount, TradeSideEnum side, Boolean confirm) {
        Boolean result = this.dealExchange(bot, uid, messageId, amount, side, confirm);
        if(result == null) return;
        if(result){
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealSubmitSuccess))
                    .build());
        } else {
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(CheckError.tradeAmountNotEnough)
                    .build());
        }
    }

    public void dealPumpLaunch(TokenBot bot, Long uid, Long statId, SniperMode sniperMode) {
        WalletBalanceStat stat = walletBalanceStatRepository.getById(statId);
        TokenBaseInfo info = tokenBaseInfoRepository.getById(stat.getTokenId());
        UserWallet userWallet = userWalletRepository.getById(stat.getWalletId());
        BigDecimal balance = tokenBalanceService.getTokenBalance(uid, userWallet, info);
        if(balance.compareTo(BigDecimal.ZERO) == 0) {
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(CheckError.tradeAmountNotEnough)
                    .build());
        } else {
            UserConfig userConfig = userConfigRepository.getById(uid);
            Integer dedicated = 0;
            BigDecimal slippage = userConfig.getFastSlippage();
            if(sniperMode == SniperMode.protect_mode){
                slippage = userConfig.getFastSlippage();
                dedicated = 1;
            }
            tradeService.generateSellTrade(uid, userWallet.getId(),
                    info.getAddress(), balance, userConfig.getSellFee(), slippage,
                    dedicated, null
            );
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealSubmitSuccess))
                    .build());
        }
    }

    private Boolean dealExchange(TokenBot bot, Long uid, Integer messageId, BigDecimal amount, TradeSideEnum side, Boolean confirm){
        String walletId = userActionService.getValue(uid, messageId, ActionValEnum.deal_wallet_id);
        String tokenAddress = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
        String modeString = userActionService.getValue(uid, messageId, ActionValEnum.deal_mode);
        UserConfig userConfig = userConfigRepository.getById(uid);
        BigDecimal slippage = userConfig.getFastSlippage();
        int dedicated = 0;
        if(SniperMode.protect_mode.getValue().equals(modeString)){
            slippage = userConfig.getProtectSlippage();
            dedicated = 1;
        }
        if(side == TradeSideEnum.buy){
            UserWallet wallet = userWalletRepository.ownedByUid(Long.valueOf(walletId), uid);
            TokenBaseInfo mainToken = tokenInfoService.getMain();
            BigDecimal balance = tokenBalanceService.getTokenBalance(uid, wallet, mainToken);
            if(balance.compareTo(amount) < 0) {
                return false;
            }
            if(!confirm && amount.compareTo(new BigDecimal(confirmLimit)) >= 0) {
                this.pushConfirm(bot, uid, messageId, amount, amount, side.getValue(), SniperMode.valueOf(modeString));
                return null;
            } else {
                tradeService.generateBuyTrade(uid, Long.valueOf(walletId),
                        tokenAddress, amount, userConfig.getBuyFee(), slippage,
                        dedicated, null
                );
            }
        }
        if(side == TradeSideEnum.sell){
            TokenBaseInfo token = tokenInfoService.geneTokenBaseInfo(tokenAddress);
            UserWallet wallet = userWalletRepository.ownedByUid(Long.valueOf(walletId), uid);
            BigDecimal balance = tokenBalanceService.getTokenBalance(uid, wallet, token);
            TokenBaseInfo tokenMain = tokenInfoService.getMain();
            if(balance.compareTo(BigDecimal.ZERO) <= 0) return false;
            BigDecimal amountBal = balance.multiply(amount);
            BigDecimal solVal = amountBal.multiply(new BigDecimal(token.getPrice())).divide(new BigDecimal(tokenMain.getPrice()),4, RoundingMode.HALF_DOWN);
            if(!confirm && solVal.compareTo(new BigDecimal(confirmLimit)) >= 0) {
                this.pushConfirm(bot, uid, messageId, solVal, amount, side.getValue(), SniperMode.valueOf(modeString));
                return null;
            } else {
                tradeService.generateSellTrade(uid, Long.valueOf(walletId),
                        tokenAddress, amountBal, userConfig.getSellFee(), slippage,
                        dedicated, null
                );
            }

        }
        return true;
    }


    public void dealIcebergAmount(TokenBot bot, Long uid, Integer messageId, BigDecimal amount, TradeSideEnum side, Long count) {
        if(this.dealIcebergExchange(bot, uid, messageId, amount, side, count)){
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.dealSubmitSuccess))
                    .build());
        } else {
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(CheckError.tradeAmountNotEnough)
                    .build());
        }
    }


    private Boolean dealIcebergExchange(TokenBot bot, Long uid, Integer messageId, BigDecimal amount, TradeSideEnum side, Long count){
        String walletId = userActionService.getValue(uid, messageId, ActionValEnum.deal_wallet_id);
        String tokenAddress = userActionService.getValue(uid, messageId, ActionValEnum.deal_token_address);
        String modeString = userActionService.getValue(uid, messageId, ActionValEnum.deal_mode);
        UserConfig userConfig = userConfigRepository.getById(uid);
        BigDecimal slippage = userConfig.getFastSlippage();
        int dedicated = 1;
//        if(SniperMode.protect_mode.getValue().equals(modeString)){
//            slippage = userConfig.getProtectSlippage();
//            dedicated = 1;
//        }
        if(side == TradeSideEnum.buy){
            UserWallet wallet = userWalletRepository.ownedByUid(Long.valueOf(walletId), uid);
            TokenBaseInfo mainToken = tokenInfoService.getMain();
            BigDecimal balance = tokenBalanceService.getTokenBalance(uid, wallet, mainToken);
            if(balance.compareTo(amount.multiply(new BigDecimal(count))) < 0) {
                return false;
            }
            IcebergTask icebergTask = icebergService.generateTask(uid, count);
            for(int i =0; i < count; i ++) {
                tradeService.generateBuyTrade(uid, Long.valueOf(walletId),
                        tokenAddress, amount, userConfig.getBuyFee(), slippage,
                        dedicated, icebergTask.getId()
                );
            }
        }
        if(side == TradeSideEnum.sell){
            TokenBaseInfo token = tokenInfoService.geneTokenBaseInfo(tokenAddress);
            UserWallet wallet = userWalletRepository.ownedByUid(Long.valueOf(walletId), uid);
            BigDecimal balance = tokenBalanceService.getTokenBalance(uid, wallet, token);
            if(balance.compareTo(BigDecimal.ZERO) <= 0) return false;
            if(amount.multiply(new BigDecimal(count)).compareTo(BigDecimal.ONE) > 0) return false;
            BigDecimal perAmount = balance.movePointRight(token.getDecimals()).multiply(amount).setScale(0, RoundingMode.HALF_DOWN);
            IcebergTask icebergTask = icebergService.generateTask(uid, count);
            List<BigDecimal> nAmount = new ArrayList<>(Collections.nCopies(count.intValue(), perAmount));
            if(amount.multiply(new BigDecimal(count)).compareTo(BigDecimal.ONE) == 0) {
                nAmount.set(count.intValue() - 1, balance.movePointRight(token.getDecimals()).subtract(perAmount.multiply(new BigDecimal(count.intValue() - 1))));
            }
            for(int i =0; i < count; i ++) {
                tradeService.generateSellTrade(uid, Long.valueOf(walletId),
                        tokenAddress, nAmount.get(i).movePointLeft(token.getDecimals()), userConfig.getSellFee(), slippage,
                        dedicated, icebergTask.getId()
                );
            }
        }
        return true;
    }

    @Resource
    private BotStateService botStateService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private UserActionService userActionService;
    @Resource
    private TradeService tradeService;
    @Resource
    private UserConfigRepository userConfigRepository;
    @Resource
    private BotReplyGenerateService botReplyGenerateService;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    @Lazy
    private BotManager botManager;
    @Resource
    private WalletBalanceStatRepository walletBalanceStatRepository;
    @Resource
    private TokenBalanceService tokenBalanceService;
    @Resource
    private IcebergService icebergService;
}
