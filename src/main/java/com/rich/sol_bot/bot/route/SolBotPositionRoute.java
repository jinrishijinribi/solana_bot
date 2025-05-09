package com.rich.sol_bot.bot.route;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.handler.BotPositionHandler;
import com.rich.sol_bot.bot.handler.BotStateService;
import com.rich.sol_bot.bot.handler.constants.BotReplyPositionConstants;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class SolBotPositionRoute {
    public void handlerCallbackQuery(TokenBot bot, Update update) {
        String content = update.getCallbackQuery().getData();
        Long uid = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        if(content.startsWith(BotReplyPositionConstants.positionWalletChooseCli)) {
            String walletId = content.replace(BotReplyPositionConstants.positionWalletChooseCli, "");
            userActionService.setValue(uid, messageId, ActionValEnum.position_wallet_id, walletId);
            userActionService.setValue(uid, messageId, ActionValEnum.position_page, "1");
            botPositionHandler.showWalletPositionPrefix(bot, uid, messageId, Long.valueOf(walletId), 1L);
        }

        switch (content) {
            case BotReplyPositionConstants.positionRefreshCli -> {
                botPositionHandler.showWalletPositionPrefix(bot, uid, messageId, null, null);
            }
            case BotReplyPositionConstants.positionSwitchWalletCli, BotReplyPositionConstants.positionReturnToWalletListCli -> {
                botPositionHandler.walletKeyBoard(bot, uid, messageId);
            }
            case BotReplyPositionConstants.positionPrePageCli -> {
                String pageStr = userActionService.getValue(uid, messageId, ActionValEnum.position_page);
                long page = 1L;
                if(pageStr != null){
                    page = Long.parseLong(pageStr);
                }
                if(page > 1) {
                    page -= 1;
                }
                userActionService.setValue(uid, messageId, ActionValEnum.position_page, Long.toString(page));
                botPositionHandler.showWalletPositionPrefix(bot, uid, messageId, null, page);
            }
            case BotReplyPositionConstants.positionNextPageCli -> {
                String pageStr = userActionService.getValue(uid, messageId, ActionValEnum.position_page);
                long page = 1L;
                if(pageStr != null){
                    page = Long.parseLong(pageStr);
                }
                page += 1;
                userActionService.setValue(uid, messageId, ActionValEnum.position_page, Long.toString(page));
                botPositionHandler.showWalletPositionPrefix(bot, uid, messageId, null, page);
            }
        }
    }

    @Resource
    private UserActionService userActionService;
    @Resource
    private BotPositionHandler botPositionHandler;
}
