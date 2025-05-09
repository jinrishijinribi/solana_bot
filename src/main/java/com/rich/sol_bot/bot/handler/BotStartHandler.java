package com.rich.sol_bot.bot.handler;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyStartConstants;
import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.bot.route.SolBotStateRoute;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.pump.PumpInfoService;
import com.rich.sol_bot.pump.mapper.PumpPoolInfo;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.service.TokenBalanceService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.UserService;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import com.rich.sol_bot.user.mapper.User;
import com.rich.sol_bot.user.mapper.UserRepository;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class BotStartHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private I18nTranslator i18nTranslator;

    public void handlerStartReplyKeyBoard(TokenBot bot, Long uid, Integer messageId) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> replyKeyboard = new ArrayList<>();

        // 第一行按钮
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        I18nLanguageEnum language = bot.getLanguage(uid);
        keyboardFirstRow.add(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.initStartAutoSniperText));
        keyboardFirstRow.add(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.initStartDealText));

        // 第二行按钮
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.initStartOwnedSniperText));
        keyboardSecondRow.add(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.initStartOwnedLimitOrderText));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.initStartPositionText));
        keyboardThirdRow.add(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.initStartWalletText));

        KeyboardRow keyboardForthRow = new KeyboardRow();
        keyboardForthRow.add(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.initStartSettingText));
        keyboardForthRow.add(i18nTranslator.getAndRenderContent(language, BotReplyAllConstants.returnToInitStartText2));

        // 添加按钮列表到键盘布局
        replyKeyboard.add(keyboardFirstRow);
        replyKeyboard.add(keyboardSecondRow);
        replyKeyboard.add(keyboardThirdRow);
        replyKeyboard.add(keyboardForthRow);

        // 设置键盘布局到回复消息中
        replyKeyboardMarkup.setKeyboard(replyKeyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.reply_keyboard)
                .chatId(uid).messageId(messageId).text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStart))
                .replyKeyboard(replyKeyboardMarkup)
                .consumer(x -> {
                    log.info("init start, {}", x);
                })
                .build());
    }

    public void handlerStart(TokenBot bot, Long uid, Integer messageId) {
        UserWallet userWallet = userWalletRepository.getPreferWallet(uid, null);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        // 刷新
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartRefreshText))
                                .callbackData(BotReplyStartConstants.initStartRefreshCli).build()
                )
        );
        // 自动狙击 + 买/卖
        keyboard.add(Arrays.asList(
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartAutoSniperText))
                        .callbackData(BotReplyStartConstants.initStartAutoSniperCli).build(),
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartDealText))
                        .callbackData(BotReplyStartConstants.initStartDealCli).build()
                )
        );
        // 已有的狙击 + 已设置挂单
        keyboard.add(Arrays.asList(
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartOwnedSniperText))
                        .callbackData(BotReplyStartConstants.initStartOwnedSniperCli).build(),
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartOwnedLimitOrderText))
                        .callbackData(BotReplyStartConstants.initStartOwnedLimitOrderCli).build())
        );
        // 刮刀
//        keyboard.add(Collections.singletonList(
//                InlineKeyboardButton.builder().text(BotReplyStartConstants.initStartScraperText)
//                        .callbackData(BotReplyStartConstants.initStartScraperCli).build())
//        );
        // 持仓 + 钱包
        keyboard.add(Arrays.asList(
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartPositionText))
                        .callbackData(BotReplyStartConstants.initStartPositionCli).build(),
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartWalletText))
                        .callbackData(BotReplyStartConstants.initStartWalletCli).build())
        );
        // 设置 + 帮助
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartSettingText))
                                .callbackData(BotReplyStartConstants.initStartSettingCli).build(),
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartHelpText))
                        .callbackData(BotReplyStartConstants.initStartHelpCli).build()
                )
        );
        // 邀请
        keyboard.add(Arrays.asList(
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartInviteText))
                        .callbackData(BotReplyStartConstants.initStartInviteCli).build(),
                InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartLanguageText))
                        .callbackData(BotReplyStartConstants.initStartLanguageCli).build())
        );
        markup.setKeyboard(keyboard);
        String content = null;
        if(userWallet == null) {
            content = i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartNoWallet);
        } else {
            TokenBaseInfo tokenBaseInfo = tokenInfoService.getMain();
            BigDecimal amount = tokenBalanceService.getMainBalance(uid, userWallet.getAddress());
            content = String.format(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartDefaultWallet),
                    userWallet.getAddress(),
                    NumberFormatTool.formatNumber(amount, 4),
                    NumberFormatTool.formatNumber(amount.multiply(new BigDecimal(tokenBaseInfo.getPrice())), 4));
        }
        bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.inline_keyboard)
                        .chatId(uid).messageId(messageId).inlineKeyboardMarkup(markup)
                        .text(content)
                .build());
        botStateService.unlockState(uid);
    }

    public void handlerDirectAddress(TokenBot bot, Update update, String tokenAddress) {
        TokenBaseInfo tokenBaseInfo = tokenInfoService.geneTokenBaseInfo(tokenAddress);
        if(tokenBaseInfo.getPoolStartTime() != null) {
            if(tokenBaseInfo.getPoolStartTime() < TimestampUtil.now().getTime()/1000) {
                // 开始交易的币
                solBotStateRoute.handlerWithState(bot, update, LockStateEnum.deal_to_choose_token, tokenAddress);
            } else {
                // 未开始交易的币
                solBotStateRoute.handlerWithState(bot, update, LockStateEnum.sniper_input_token, tokenAddress);
            }
        }
        // 未上池的币 判断是否上了pump，以及pump上是否已经购买完
        else {
            PumpPoolInfo pumpPoolInfo = pumpInfoService.getPumpInfo(tokenAddress);
//            if(pumpPoolInfo != null && pumpPoolInfo.getComplete() != 1) {
            if(pumpPoolInfo != null) {
                solBotStateRoute.handlerWithState(bot, update, LockStateEnum.deal_to_choose_token, tokenAddress);
            } else {
                solBotStateRoute.handlerWithState(bot, update, LockStateEnum.sniper_input_token, tokenAddress);
            }
        }

    }

    public void startHelp(TokenBot bot, Long uid) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text)
                .chatId(uid).text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.initStartHelpContent))
                .build());
    }

    public void checkUser(Update update, String refCode) {
        Long id = update.getMessage().getFrom().getId();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        String username = update.getMessage().getFrom().getUserName();
        userService.generateUser(User.builder()
                .id(id).firstName(firstName).lastName(lastName)
                .username(username).lastActive(TimestampUtil.now()).createdAt(TimestampUtil.now())
                .refCode(RandomStringUtils.random(6, "abcdefghijklmnopqrstuvwxyz1234567890"))
                .build(),
                refCode
        );
    }

    public void activeUser(Long id) {
        userRepository.update(
                new LambdaUpdateWrapper<User>()
                        .set(User::getLastActive, TimestampUtil.now())
                        .eq(User::getId, id)
        );
    }

    @Resource
    private UserService userService;
    @Resource
    private BotStateService botStateService;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    @Lazy
    private SolBotStateRoute solBotStateRoute;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private TokenBalanceService tokenBalanceService;
    @Resource
    private PumpInfoService pumpInfoService;

}
