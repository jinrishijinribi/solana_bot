package com.rich.sol_bot.bot.handler;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyStartConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyWalletConstants;
import com.rich.sol_bot.bot.handler.enums.WalletActionEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.sol.entity.SolBalance;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.service.TokenBalanceService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.action.ActionEnum;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.user.withdraw.UserWithdrawService;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.WalletBalanceStatService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStatRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BotWalletHandler {
    @Autowired
    private I18nTranslator i18nTranslator;

    public void noWalletStart(TokenBot bot, Long uid, Integer messageId) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletCreateText))
                                .callbackData(BotReplyWalletConstants.walletCreateCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletImportText))
                                .callbackData(BotReplyWalletConstants.walletImportCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                                .callbackData(BotReplyStartConstants.returnToInitStartCli).build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId).text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.createWalletStart))
                .inlineKeyboardMarkup(markup)
                .build());
    }

    public void walletKeyBoard(TokenBot bot, Long uid, Integer messageId) {
        List<UserWallet> wallets = userWalletService.walletList(uid);
        StringBuilder content = new StringBuilder();
        content.append(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletListTitle));
        TokenBaseInfo baseInfo = tokenInfoService.getMain();

        for(UserWallet w: wallets) {
            BigDecimal amount = tokenBalanceService.getTokenBalance(uid, w, baseInfo);
            content.append(
                    String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletShowContent), w.getName(), w.getAddress(),
                            NumberFormatTool.formatNumber(amount, 4))
            );
        }
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletCreateText))
                                .callbackData(BotReplyWalletConstants.walletCreateCli).build(),
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletImportText))
                        .callbackData(BotReplyWalletConstants.walletImportCli).build()
                )
        );
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletRemoveText))
                                .callbackData(BotReplyWalletConstants.walletRemoveCli).build(),
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletSetNameText))
                                .callbackData(BotReplyWalletConstants.walletSetNameCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletExportPriText))
                                .callbackData(BotReplyWalletConstants.walletExportPriCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletTransferSOLText))
                                .callbackData(BotReplyWalletConstants.walletTransferSOLCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                                .callbackData(BotReplyStartConstants.returnToInitStartCli).build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId).text(content.toString())
                .inlineKeyboardMarkup(markup)
                .build());
    }

    public void createWalletName(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null).text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.createWalletName))
                .build());
    }

    public void createWalletSuccess(TokenBot bot, Long uid, String name) {
        UserWallet wallet = userWalletService.generateWallet(uid, name);
        walletBalanceStatService.getBalanceStatAndSync(uid, wallet, tokenInfoService.getMain(), true);
        String content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.createWalletSuccess), userWalletService.getPriKey(wallet), wallet.getAddress());
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null).text(content)
                .build());
        botStateService.unlockState(uid);
    }

    public void importWalletName(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null).text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.createWalletName))
                .build());
    }

    public void importWalletPri(TokenBot bot, Long uid, String name) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null).text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.importWalletKey))
                .build());
//            botStateService.storeValue(uid, LockStateEnum.wallet_import_step_2, name);
        userActionService.setValue(uid, ActionValEnum.wallet_name, name);
    }

    public void importWalletSuccess(TokenBot bot, Long uid, String priKey) {
        String name = userActionService.getValue(uid, ActionValEnum.wallet_name);
        if(name == null) {
            name = "default";
        }
        UserWallet wallet = userWalletService.generateWallet(uid, name, priKey);
        WalletBalanceStat stat = walletBalanceStatService.getBalanceStatAndSync(uid, wallet, tokenInfoService.getMain(), true);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.importWalletSuccess), wallet.getAddress(), stat.getAmount().stripTrailingZeros().toPlainString()))
                .build());
        botStateService.unlockState(uid);
        userActionService.actionFinish(uid, ActionEnum.wallet);
    }

    public void walletToSelect(TokenBot bot, Long uid, WalletActionEnum action, Integer messageId) {
        String content = i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletToChooseContent);
        List<UserWallet> wallets = userWalletService.walletList(uid);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(UserWallet w: wallets) {
            keyboard.add(Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(w.getName())
                                    .callbackData(BotReplyWalletConstants.walletChooseByType(action, w.getId()))
                                    .build()
                    )
            );
        }
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(null)
                .text(content).inlineKeyboardMarkup(markup)
                .build());
    }

    public void toSetWalletName(TokenBot bot, Long uid) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletToSetNameContent))
                .build());
    }

    public void toTransferSol(TokenBot bot, Long uid) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletToTransferSolContent))
                .build());
    }

    public void toExportPri(TokenBot bot, Long uid, Long walletId) {
        String priKey = userWalletService.getPriKey(uid, walletId);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(priKey)
                .build());
    }


    public Boolean handlerTransferSolAmount(TokenBot bot, Long uid, String content) {
        String walletId = userActionService.getValue(uid, ActionValEnum.wallet_transfer_sol_id);
        UserWallet wallet = userWalletRepository.ownedByUid(Long.valueOf(walletId), uid);
        SolBalance solBalance = solQueryService.solBalance(wallet.getAddress());
        if(new BigDecimal(solBalance.getBalance()).compareTo(new BigDecimal(content)) >= 0){
            userActionService.setValue(uid, ActionValEnum.wallet_transfer_sol_amount, content);
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletTransferInputAddress))
                    .build());
            return true;
        } else {
            return false;
        }
    }

    public void handlerTransferSolAddress(TokenBot bot, Long uid, String address) {
        String walletId = userActionService.getValue(uid, ActionValEnum.wallet_transfer_sol_id);
        String amount = userActionService.getValue(uid, ActionValEnum.wallet_transfer_sol_amount);
        userWithdrawService.withdrawWallet(uid, Long.valueOf(walletId), new BigDecimal(amount), address);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletTransferSubmitSuccess))
                .build());
    }

    @Resource
    private BotStateService botStateService;
    @Resource
    private UserWalletService userWalletService;
    @Resource
    private UserActionService userActionService;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private WalletBalanceStatService walletBalanceStatService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private SolQueryService solQueryService;
    @Resource
    private UserWithdrawService userWithdrawService;
    @Resource
    private TokenBalanceService tokenBalanceService;

}
