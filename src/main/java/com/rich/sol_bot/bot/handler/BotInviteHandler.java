package com.rich.sol_bot.bot.handler;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyInviteConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyStartConstants;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.user.balance.mapper.UserBalance;
import com.rich.sol_bot.user.balance.mapper.UserBalanceRepository;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.user.mapper.*;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BotInviteHandler {

    @Autowired
    private I18nTranslator i18nTranslator;

    public void inviteKeyboard(TokenBot bot, Long uid, Integer messageId) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(
                                        i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.inviteRefreshText)
                                )
                                .callbackData(BotReplyInviteConstants.inviteRefreshCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(
                                        i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.inviteWithdrawText)
                                )
                                .callbackData(BotReplyInviteConstants.inviteWithdrawCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                                .callbackData(BotReplyStartConstants.returnToInitStartCli).build()
                )
        );
        markup.setKeyboard(keyboard);
        UserBalance balance = userBalanceRepository.getOrCreateMain(uid);
        Long sonCount = userRelationRepository.countSon(uid);
        User user = userRepository.getById(uid);
        UserConfig userConfig = userConfigRepository.getById(user.getId());
        String content = String.format(
                i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.inviteKeyBoardContent),
                "https://t.me/" +bot.getBotUsername()+ "?start=" + user.getRefCode(),
                balance.getRemain().stripTrailingZeros().toPlainString(), balance.getFreeze().stripTrailingZeros().toPlainString(),
                0, sonCount, userConfig.getRebateRate().movePointRight(2).stripTrailingZeros().toPlainString() + "%"
        );
        bot.sendMessageToQueue(BotPushMessage.builder()
                        .chatId(uid).messageId(messageId).text(content).inlineKeyboardMarkup(markup)
                .type(BotMessageTypeEnum.inline_keyboard).disableMarkdown(true)
                .build());
    }

    public void inviteWalletToChoose(TokenBot bot, Long uid, Integer messageId) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<UserWallet> wallets = userWalletService.walletList(uid);
        for(UserWallet w: wallets) {
            keyboard.add(Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(w.getName())
                                    .callbackData(BotReplyInviteConstants.inviteChooseWalletPrefix + w.getId())
                                    .build()
                    )
            );
        }
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.inviteChooseWalletOtherText))
                                .callbackData(BotReplyInviteConstants.inviteChooseWalletOtherCli)
                                .build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.inviteChooseWalletReturnText))
                                .callbackData(BotReplyInviteConstants.inviteChooseWalletReturnCli)
                                .build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.inviteChooseWalletHead))
                .inlineKeyboardMarkup(markup)
                .build());
    }

    public void inviteWaitWalletInput(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.inviteWaitWalletInput))
                .build());
    }

    @Resource
    private UserWalletService userWalletService;
    @Resource
    private UserBalanceRepository userBalanceRepository;
    @Resource
    private UserRelationRepository userRelationRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserConfigRepository userConfigRepository;
}
