package com.rich.sol_bot.bot.route;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.CheckError;
import com.rich.sol_bot.bot.handler.BotInviteHandler;
import com.rich.sol_bot.bot.handler.BotStateService;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyInviteConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyWalletConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.user.balance.UserBalanceService;
import com.rich.sol_bot.user.balance.mapper.UserBalance;
import com.rich.sol_bot.user.withdraw.UserWithdrawService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;

@Service
public class SolBotInviteRoute {

    @Autowired
    private I18nTranslator i18nTranslator;

    public void handlerCallbackQuery(TokenBot bot, Update update) {
        String content = update.getCallbackQuery().getData();
        Long uid = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
//        String callBackDataId = update.getCallbackQuery().getId();
        if(content.startsWith(BotReplyInviteConstants.inviteChooseWalletPrefix)) {
            String walletId = content.replace(BotReplyInviteConstants.inviteChooseWalletPrefix, "");
            UserWallet wallet = userWalletRepository.getById(walletId);
            if(!userWithdrawService.withdrawRebate(uid, wallet.getAddress())) {
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.simple_text)
                        .chatId(uid).messageId(null)
                        .text(CheckError.withdrawRebateFail)
                        .build());
            } else {
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.simple_text)
                        .chatId(uid).messageId(null)
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.walletTransferSubmitSuccess))
                        .build());
            };
            return;
        }

        switch (content) {
            case BotReplyInviteConstants.inviteRefreshCli, BotReplyInviteConstants.inviteChooseWalletReturnCli -> {
                botInviteHandler.inviteKeyboard(bot, uid, messageId);
            }
            case BotReplyInviteConstants.inviteWithdrawCli -> {
                UserBalance userBalance = userBalanceService.getMainUserBalance(uid);
                if(userBalance.getRemain().compareTo(new BigDecimal("0.01")) < 0) {
                    bot.sendMessageToQueue(BotPushMessage.builder()
                            .type(BotMessageTypeEnum.simple_text)
                            .chatId(uid).messageId(null)
                            .text(CheckError.withdrawRebateFail)
                            .build());
                    return;
                }
                botInviteHandler.inviteWalletToChoose(bot, uid, messageId);
            }
            case BotReplyInviteConstants.inviteChooseWalletOtherCli -> {
                botInviteHandler.inviteWaitWalletInput(bot, uid, messageId);
                botStateService.lockState(uid, LockStateEnum.invite_input_wallet);
            }
        }
    }


    @Resource
    private BotInviteHandler botInviteHandler;
    @Resource
    private BotStateService botStateService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private UserWithdrawService userWithdrawService;
    @Resource
    private UserBalanceService userBalanceService;
}
