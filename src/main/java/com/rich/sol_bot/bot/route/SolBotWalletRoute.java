package com.rich.sol_bot.bot.route;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.handler.BotDealHandler;
import com.rich.sol_bot.bot.handler.BotStartHandler;
import com.rich.sol_bot.bot.handler.BotStateService;
import com.rich.sol_bot.bot.handler.BotWalletHandler;
import com.rich.sol_bot.bot.handler.constants.BotReplyWalletConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.handler.enums.WalletActionEnum;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.wallet.UserWalletService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class SolBotWalletRoute {

    public void handlerCallbackQuery(TokenBot bot, Update update) {
        String content = update.getCallbackQuery().getData();
        Long uid = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        switch (content) {
            // 创建钱包
            case BotReplyWalletConstants.walletCreateCli -> {
                botWalletHandler.createWalletName(bot, uid, messageId);
                botStateService.lockState(uid, LockStateEnum.wallet_create_name);
            }
            // 导入钱包
            case BotReplyWalletConstants.walletImportCli -> {
                botWalletHandler.importWalletName(bot, uid, messageId);
                botStateService.lockState(uid, LockStateEnum.wallet_import_name);
            }
            case BotReplyWalletConstants.walletRemoveCli -> {
                botWalletHandler.walletToSelect(bot, uid, WalletActionEnum.to_remove, messageId);
            }
            case BotReplyWalletConstants.walletSetNameCli -> {
                botWalletHandler.walletToSelect(bot, uid, WalletActionEnum.to_set_name, messageId);
            }
            case BotReplyWalletConstants.walletExportPriCli -> {
                botWalletHandler.walletToSelect(bot, uid, WalletActionEnum.to_export_pri, messageId);
            }
            case BotReplyWalletConstants.walletTransferSOLCli -> {
                botWalletHandler.walletToSelect(bot, uid, WalletActionEnum.to_transfer_sol, messageId);
            }
        }
        if(content.startsWith(BotReplyWalletConstants.walletToChooseActionPrefix)){
            String[] choose = content.replace(BotReplyWalletConstants.walletToChooseActionPrefix, "").split(":");
            WalletActionEnum action = null;
            String walletId = "";
            if(choose.length == 2) {
                action = WalletActionEnum.valueOf(choose[0]);
                walletId = choose[1];

                switch (action) {
                    case to_remove -> {
                        userWalletService.removeWallet(Long.valueOf(walletId));
                        botWalletHandler.walletKeyBoard(bot, uid, messageId);
                    }
                    case to_set_name -> {
                        botWalletHandler.toSetWalletName(bot, uid);
                        userActionService.setValue(uid, ActionValEnum.wallet_change_name_id, walletId);
                        botStateService.lockState(uid, LockStateEnum.wallet_change_name);
                    }
                    case to_export_pri -> {
                        botWalletHandler.toExportPri(bot, uid, Long.valueOf(walletId));
                        botWalletHandler.walletKeyBoard(bot, uid, messageId);
                    }
                    case to_transfer_sol -> {
                        botWalletHandler.toTransferSol(bot, uid);
                        userActionService.setValue(uid, ActionValEnum.wallet_transfer_sol_id, walletId);
                        botStateService.lockState(uid, LockStateEnum.wallet_transfer_sol);
                    }
                }
            }
        }
    }

    @Resource
    private BotWalletHandler botWalletHandler;
    @Resource
    private UserWalletService userWalletService;
    @Resource
    private UserActionService userActionService;
    @Resource
    private BotStateService botStateService;
}
