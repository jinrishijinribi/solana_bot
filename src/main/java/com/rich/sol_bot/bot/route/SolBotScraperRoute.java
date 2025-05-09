package com.rich.sol_bot.bot.route;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.CheckService;
import com.rich.sol_bot.bot.handler.BotScraperHandler;
import com.rich.sol_bot.bot.handler.BotStateService;
import com.rich.sol_bot.bot.handler.BotWalletHandler;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyScraperConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.scraper.mapper.UserScraperTaskRepository;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.user.action.mapper.UserAction;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;

@Service
public class SolBotScraperRoute {
    private final UserWalletService userWalletService;
    private final BotWalletHandler botWalletHandler;
    private final BotScraperHandler botScraperHandler;
    private final CheckService checkService;
    private final UserActionService userActionService;
    private final UserWalletRepository userWalletRepository;
    private final UserScraperTaskRepository userScraperTaskRepository;
    private final BotStateService botStateService;
    private final I18nTranslator i18nTranslator;

    public SolBotScraperRoute(UserWalletService userWalletService, BotWalletHandler botWalletHandler, BotScraperHandler botScraperHandler, CheckService checkService, UserActionService userActionService, UserWalletRepository userWalletRepository, UserScraperTaskRepository userScraperTaskRepository, BotStateService botStateService, I18nTranslator i18nTranslator) {
        this.userWalletService = userWalletService;
        this.botWalletHandler = botWalletHandler;
        this.botScraperHandler = botScraperHandler;
        this.checkService = checkService;
        this.userActionService = userActionService;
        this.userWalletRepository = userWalletRepository;
        this.userScraperTaskRepository = userScraperTaskRepository;
        this.botStateService = botStateService;
        this.i18nTranslator = i18nTranslator;
    }

    public void handlerCallbackQuery(TokenBot bot, Update update) {
        // 按钮返回内容
        String content = update.getCallbackQuery().getData();
        Long uid = update.getCallbackQuery().getFrom().getId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        // 展示列表
        // 修改gas
        // 修改执行次数
        // 切换钱包
        // 切换刮刀任务
        // 切换交易模式
        // 切换交易金额
        // 等待输入交易金额
        // 输入交易金额
        // 创建新任务
        // 删除任务
        // 开启关闭自动买入卖出
        // 返回
        // 输入 twitter名字

        // 刷新列表
        if(content.startsWith(BotReplyScraperConstants.scraperRefreshCliPrefix)){
            String taskId = content.replace(BotReplyScraperConstants.scraperRefreshCliPrefix, "");
            botScraperHandler.listScraper(bot, uid, messageId, Long.valueOf(taskId));
        }
        // 切换钱包
        if(content.startsWith(BotReplyScraperConstants.scraperSetWalletCli)){
            String walletId = content.replace(BotReplyScraperConstants.scraperSetWalletCli, "");
            String taskId = userActionService.getValue(uid, messageId, ActionValEnum.scraper_keyboard_task_id);
            UserWallet userWallet = userWalletRepository.ownedByUid(Long.valueOf(walletId), uid);
            if(userWallet != null) {
                userScraperTaskRepository.updateWalletId(Long.valueOf(taskId), uid, Long.valueOf(walletId));
            }
            botScraperHandler.listScraper(bot, uid, messageId, Long.valueOf(taskId));
        }
        // 切换任务
        if(content.startsWith(BotReplyScraperConstants.scraperSelectTaskCli)) {
            String taskId = content.replace(BotReplyScraperConstants.scraperSelectTaskCli, "");
            botScraperHandler.listScraper(bot, uid, messageId, Long.valueOf(taskId));
        }
        // 修改金额
        if(content.startsWith(BotReplyScraperConstants.scraperBuyPrefix)){
            String value = content.replace(BotReplyScraperConstants.scraperBuyPrefix, "");
            String[] v = value.split(":");
            if(v.length != 2) return;
            String taskId = v[0];
            String amount = v[1];
            if(CheckService.isNumeric(amount)) {
                userScraperTaskRepository.updateAmount(Long.valueOf(taskId), uid, new BigDecimal(amount));
            } else {
                userActionService.setValue(uid, ActionValEnum.scraper_keyboard_count_task_id,taskId);
                bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.simple_text)
                        .chatId(uid).messageId(null)
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperStartInputAmountMsg))
                        .build());
                botStateService.lockState(uid, LockStateEnum.scraper_modify_amount);
            }
            botScraperHandler.listScraper(bot, uid, messageId, Long.valueOf(taskId));
        }
        switch (content) {
            // 创建任务
            case BotReplyScraperConstants.scraperNewTaskCli -> {
                if(!userWalletService.walletExist(uid)){
                    botWalletHandler.noWalletStart(bot, uid, null);
                } else {
                    botScraperHandler.chooseToImportTwitterName(bot, uid, null);
                }
            }
            // 修改次数
            case BotReplyScraperConstants.scraperCountCli -> {
                String taskId = userActionService.getValue(uid, messageId, ActionValEnum.scraper_keyboard_task_id);
                userActionService.setValue(uid, ActionValEnum.scraper_keyboard_count_task_id, taskId);
                botScraperHandler.chooseToImportCount(bot, uid, null);
            }
            // 修改快速模式
            case BotReplyScraperConstants.scraperFastModeCli -> {
                String taskId = userActionService.getValue(uid, messageId, ActionValEnum.scraper_keyboard_task_id);
                userScraperTaskRepository.updateMode(Long.valueOf(taskId), uid, SniperMode.fast_mode);
                botScraperHandler.listScraper(bot, uid, messageId, Long.valueOf(taskId));
            }
            // 修改防夹模式
            case BotReplyScraperConstants.scraperProtectModeCli -> {
                String taskId = userActionService.getValue(uid, messageId, ActionValEnum.scraper_keyboard_task_id);
                userScraperTaskRepository.updateMode(Long.valueOf(taskId), uid, SniperMode.protect_mode);
                botScraperHandler.listScraper(bot, uid, messageId, Long.valueOf(taskId));
            }
            // 删除任务
            case BotReplyScraperConstants.scraperRmTaskCli -> {
                String taskId = userActionService.getValue(uid, messageId, ActionValEnum.scraper_keyboard_task_id);
                userScraperTaskRepository.removeTask(Long.valueOf(taskId), uid);
                botScraperHandler.listScraper(bot, uid, messageId, null);
            }
            //
        }
    }
}
