package com.rich.sol_bot.bot.handler;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.bot.handler.constants.*;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.scraper.mapper.UserScraperTask;
import com.rich.sol_bot.scraper.mapper.UserScraperTaskRepository;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.service.TokenBalanceService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.twitter.TwitterSearchService;
import com.rich.sol_bot.twitter.mapper.TwitterUser;
import com.rich.sol_bot.twitter.mapper.TwitterUserRepository;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
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
public class BotScraperHandler {
    @Resource
    private UserScraperTaskRepository userScraperTaskRepository;
    @Autowired
    private UserWalletRepository userWalletRepository;
    @Autowired
    private TokenInfoService tokenInfoService;
    @Autowired
    private TokenBalanceService tokenBalanceService;
    @Autowired
    private UserConfigRepository userConfigRepository;
    @Autowired
    private BotStateService botStateService;
    @Autowired
    private TwitterSearchService twitterSearchService;
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private TwitterUserRepository twitterUserRepository;
    @Autowired
    private I18nTranslator i18nTranslator;

    // 等待输入任务次数
    public void chooseToImportCount(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperCountImportMsg))
                .build());
        botStateService.lockState(uid, LockStateEnum.scraper_input_count);
    }

    // 创建任务
    public void generateTask(TokenBot tokenBot, Long uid, Integer messageId, BigDecimal amount) {
        String username = userActionService.getValue(uid, ActionValEnum.scraper_twitter_username);
        if(username == null) return;
        UserConfig userConfig = userConfigRepository.getById(uid);
        TwitterUser twitterUser = twitterUserRepository.findByUsername(username);
        if(twitterUser == null) return;
        UserWallet userWallet = userWalletRepository.getPreferWallet(uid, userConfig.getPreferWallet());
        if(userWallet == null) return;
        UserScraperTask task = UserScraperTask.builder()
                .id(IdUtil.nextId()).uid(uid).twitterUsername(username).twitterUserId(twitterUser.getId())
                .amount(amount).count(1).successCount(0).mode(SniperMode.protect_mode)
                .walletId(userWallet.getId())
                .createdAt(TimestampUtil.now()).deleted(0)
                .build();
        userScraperTaskRepository.save(task);
        this.listScraper(tokenBot, uid, messageId, null);
    }

    // 等待输入twitter name
    public void chooseToImportTwitterName(TokenBot bot, Long uid, Integer messageId) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).messageId(null)
                .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperStartImportNameMsg))
                .build());
        botStateService.lockState(uid, LockStateEnum.scraper_input_twitter_name);
    }
    // 处理输入的twitter name
    public Boolean dealTwitterName(TokenBot bot, Long uid, Integer messageId, String content) {
        TwitterUser user = twitterSearchService.generateTwitterUser(content);
        if(user == null) {
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperStartTwitterErrMsg))
                    .build());
            return false;
        } else {
            userActionService.setValue(uid, ActionValEnum.scraper_twitter_username,content);
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.simple_text)
                    .chatId(uid).messageId(null)
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperStartInputAmountMsg))
                    .build());
            return true;
        }
    }

    // 展示列表
    public void listScraper(TokenBot bot, Long uid, Integer messageId, Long taskId) {
        List<UserScraperTask> tasks = userScraperTaskRepository.getTasksByUid(uid);
        if(tasks.isEmpty()){
            InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperNewTaskText))
                                    .callbackData(BotReplyScraperConstants.scraperNewTaskCli)
                                    .build()
                    )
            );
            keyboard.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                            .callbackData(BotReplyStartConstants.returnToInitStartCli)
                            .build()
            ));
            markup.setKeyboard(keyboard);
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.inline_keyboard)
                    .chatId(uid).messageId(messageId).inlineKeyboardMarkup(markup)
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperNoTask))
                    .build());
        } else {
            UserScraperTask selectOne = null;
            if(taskId != null) {
                List<UserScraperTask> list = tasks.stream().filter(o -> taskId.equals(o.getId())).toList();
                if(!list.isEmpty()){
                    selectOne = list.get(0);
                }
            }
            if(selectOne == null) {
                selectOne = tasks.get(0);
            }
//            taskId = selectOne.getId();
            UserConfig userConfig = userConfigRepository.getById(uid);
            InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            Long selectWalletId = selectOne.getWalletId();
//            UserWallet wallet = userWalletRepository.ownedByUid(selectOne.getWalletId(), uid);
            List<UserWallet> wallets = userWalletRepository.listByUid(uid);
            List<UserWallet> selectWallet = wallets.stream().filter(o -> selectWalletId.equals(o.getId())).toList();
            UserWallet wallet = selectWallet.get(0);
            TokenBaseInfo mainToken = tokenInfoService.getMain();
            BigDecimal balance = tokenBalanceService.getTokenBalance(uid, wallet, mainToken);
            String content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperTaskInfoTemplate),
                    (selectOne.getId() % 1000),
                    selectOne.getTwitterUsername(),
                    selectOne.getSuccessCount(),
                    NumberFormatTool.formatNumber(balance, 4),
                    selectOne.getCount(),
                    NumberFormatTool.formatNumber(selectOne.getAmount(), 4)
            );
            keyboard.add(Arrays.asList(
                    InlineKeyboardButton.builder()
                        .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperRefreshText))
                        .callbackData(BotReplyScraperConstants.scraperRefreshCliPrefix + selectOne.getId())
                        .build(),
//                    InlineKeyboardButton.builder()
//                            .text(BotReplyScraperConstants.scraperExtraGasText)
//                            .callbackData(BotReplyScraperConstants.scraperExtraGasCli)
//                            .build(),
                    InlineKeyboardButton.builder()
                            .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperCountText))
                            .callbackData(BotReplyScraperConstants.scraperCountCli)
                            .build()
            ));
            List<InlineKeyboardButton> walletButtons = new ArrayList<>();
            for(UserWallet u: wallets) {
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(BotReplySniperConstants.sniperBindButton(u.getId().equals(wallet.getId())) + u.getName())
                        .callbackData(BotReplyScraperConstants.scraperSetWalletCli + u.getId())
                        .build();
                walletButtons.add(button);
                if(walletButtons.size() == 3) {
                    keyboard.add(walletButtons);
                    walletButtons = new ArrayList<>();
                }
            }
            if(!walletButtons.isEmpty()) {
                keyboard.add(walletButtons);
            }
            List<InlineKeyboardButton> scraperTasks = new ArrayList<>();
            for(UserScraperTask u: tasks) {
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(BotReplyScraperConstants.isSelected(u.getId().equals(selectOne.getId())) + "挂刀任务:" + (u.getId() % 1000))
                        .callbackData(BotReplyScraperConstants.scraperSelectTaskCli + u.getId())
                        .build();
                scraperTasks.add(button);
                if(walletButtons.size() == 3) {
                    keyboard.add(scraperTasks);
                    scraperTasks = new ArrayList<>();
                }
            }
            if(!scraperTasks.isEmpty()) {
                keyboard.add(scraperTasks);
            }
            keyboard.add(Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(BotReplyScraperConstants.isModeOn(selectOne.getMode().equals(SniperMode.fast_mode))
                                            + i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperFastModeText))
                                    .callbackData(BotReplyScraperConstants.scraperFastModeCli)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(BotReplyScraperConstants.isModeOn(selectOne.getMode().equals(SniperMode.protect_mode))
                                            + i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperProtectModeText))
                                    .callbackData(BotReplyScraperConstants.scraperProtectModeCli)
                                    .build()
                    )
            );
            keyboard.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperAmountContent))
                    .callbackData(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperAmountContent))
                    .build()));
            String buyPrefix = i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperSendSuccess);
            keyboard.add(Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(buyPrefix + BotReplyScraperConstants.generateDealBuyText(BotReplyScraperConstants.amountList.get(0)))
                                    .callbackData(BotReplyScraperConstants.generateDealBuyCli(
                                            selectOne.getId(), BotReplyScraperConstants.amountList.get(0)))
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(buyPrefix + BotReplyScraperConstants.generateDealBuyText(BotReplyScraperConstants.amountList.get(1)))
                                    .callbackData(BotReplyScraperConstants.generateDealBuyCli(
                                            selectOne.getId(), BotReplyScraperConstants.amountList.get(1)))
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(buyPrefix + BotReplyScraperConstants.generateDealBuyText(BotReplyScraperConstants.amountList.get(2)))
                                    .callbackData(BotReplyScraperConstants.generateDealBuyCli(
                                            selectOne.getId(), BotReplyScraperConstants.amountList.get(2)))
                                    .build()
                    )
            );
            keyboard.add(Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(buyPrefix + BotReplyScraperConstants.generateDealBuyText(BotReplyScraperConstants.amountList.get(3)))
                                    .callbackData(BotReplyScraperConstants.generateDealBuyCli(
                                            selectOne.getId(), BotReplyScraperConstants.amountList.get(3)))
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(buyPrefix + BotReplyScraperConstants.generateDealBuyText(BotReplyScraperConstants.amountList.get(4)))
                                    .callbackData(BotReplyScraperConstants.generateDealBuyCli(
                                            selectOne.getId(), BotReplyScraperConstants.amountList.get(4)))
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(buyPrefix + BotReplyScraperConstants.generateDealBuyText(BotReplyScraperConstants.amountList.get(5)))
                                    .callbackData(BotReplyScraperConstants.generateDealBuyCli(
                                            selectOne.getId(), BotReplyScraperConstants.amountList.get(5)))
                                    .build()
                    )
            );

            keyboard.add(Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperNewTaskText))
                                    .callbackData(BotReplyScraperConstants.scraperNewTaskCli)
                                    .build(),
                    InlineKeyboardButton.builder()
                            .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.scraperRmTaskText))
                            .callbackData(BotReplyScraperConstants.scraperRmTaskCli)
                            .build(),
                    InlineKeyboardButton.builder()
                            .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.isModeOn(userConfig.getAutoSell() == 1)))
                            .callbackData(BotReplySettingConstants.settingAutoSellCli)
                            .build()
            ));
            keyboard.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                            .callbackData(BotReplyStartConstants.returnToInitStartCli)
                            .build()
            ));
            markup.setKeyboard(keyboard);

            Long finalTaskId = selectOne.getId();
            bot.sendMessageToQueue(BotPushMessage.builder()
                    .type(BotMessageTypeEnum.inline_keyboard)
                    .chatId(uid).messageId(messageId).inlineKeyboardMarkup(markup)
                    .text(content).consumer(m -> {
                        userActionService.setValue(uid, m, ActionValEnum.scraper_keyboard_task_id, String.valueOf(finalTaskId));
                    })
                    .build());
        }
    }
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

}
