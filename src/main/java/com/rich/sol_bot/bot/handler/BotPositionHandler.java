package com.rich.sol_bot.bot.handler;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.check.NumberFormatTool;
import com.rich.sol_bot.bot.handler.constants.*;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.trade.service.TokenPxService;
import com.rich.sol_bot.user.action.ActionValEnum;
import com.rich.sol_bot.user.action.UserActionService;
import com.rich.sol_bot.wallet.UserWalletService;
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
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BotPositionHandler {
    @Autowired
    private I18nTranslator i18nTranslator;

    public void walletKeyBoard(TokenBot bot, Long uid, Integer messageId) {
        List<UserWallet> wallets = userWalletService.walletList(uid);
        String content = i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.positionWalletChoose);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(UserWallet wallet: wallets) {
            keyboard.add(Collections.singletonList(
                            InlineKeyboardButton.builder().text(
                                    i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.positionWalletChooseText) + wallet.getName())
                                    .callbackData(BotReplyPositionConstants.positionWalletChooseCli + wallet.getId()).build()
                    )
            );
        }
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.returnToInitStartText))
                                .callbackData(BotReplyStartConstants.returnToInitStartCli).build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(content).inlineKeyboardMarkup(markup)
                .build());
    }

    public void showWalletPositionPrefix(TokenBot bot, Long uid, Integer messageId, Long walletId, Long page) {
        if(walletId == null) {
            UserWallet userWallet = userWalletRepository.getPreferWallet(uid, null);
            walletId = userWallet.getId();
        }
        if(page == null) {
            String pageStr = userActionService.getValue(uid, messageId, ActionValEnum.position_page);
            if(pageStr != null) {
                page = Long.valueOf(pageStr);
            } else {
                page = 1L;
            }
        }
        this.showWalletPosition(bot, uid, messageId, walletId, page);
    }

    public void showWalletPosition(TokenBot bot, Long uid, Integer messageId, Long walletId, Long page) {
        String botName = bot.getBotUsername();
        TokenBaseInfo main = tokenInfoService.getMain();
        List<WalletBalanceStat> stats = walletBalanceStatRepository.listExistByUidAndWalletId(uid, walletId);
        stats = stats.stream().filter(o -> !o.getTokenId().equals(main.getId())).skip((page - 1) * 5).limit(5).toList();
        List<Long> tokenIds = stats.stream().map(WalletBalanceStat::getTokenId).toList();
        List<TokenBaseInfo> tokenBaseInfos = tokenBaseInfoRepository.listByIds(tokenIds);
        Map<Long, TokenBaseInfo> map = new HashMap<>();
        tokenBaseInfos.forEach(o -> {
            map.put(o.getId(), o);
        });
        StringBuilder content = new StringBuilder();
        if(stats.isEmpty()) {
            content.append("暂无持仓");
        } else {
            content = new StringBuilder("代币名称    持仓价值SOL    持仓涨跌    持有时间 \n");
            for(WalletBalanceStat stat: stats) {
                TokenBaseInfo baseInfo = map.get(stat.getTokenId());
                BigDecimal px = tokenPxService.getPx(baseInfo);
                BigDecimal solNowVal = stat.getAmount().multiply(px.divide(new BigDecimal(main.getPrice()), RoundingMode.HALF_DOWN));
                BigDecimal uprate = BigDecimal.ZERO;
                if(stat.getVal().compareTo(BigDecimal.ZERO) > 0) {
                    uprate = solNowVal.subtract(stat.getVal()).divide(stat.getVal(), RoundingMode.HALF_DOWN);
                }
                content.append(String.format("[%s](https://t.me/%s?start=%s)    %s    %s    %s \n",
                                baseInfo.getSymbol(),
                                botName,
                                baseInfo.getAddress(),
                                NumberFormatTool.formatNumber(solNowVal,4),
                                NumberFormatTool.formatNumber(uprate.movePointRight(2), 2) + "%",
                                timeDiff(stat.getHoldStartAt())
                        )
                );
            }
        }
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        if(!stats.isEmpty()){
            keyboard.add(Arrays.asList(
                            InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.positionPrePageText))
                                    .callbackData(BotReplyPositionConstants.positionPrePageCli).build(),
                            InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.positionNextPageText))
                                    .callbackData(BotReplyPositionConstants.positionNextPageCli).build()
                    )
            );
        }
        keyboard.add(Arrays.asList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.positionRefreshText))
                                .callbackData(BotReplyPositionConstants.positionRefreshCli).build(),
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.positionSwitchWalletText))
                                .callbackData(BotReplyPositionConstants.positionSwitchWalletCli).build()
                )
        );
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder().text(i18nTranslator.getAndRenderContent(bot.getLanguage(uid), BotReplyAllConstants.positionReturnToWalletListText))
                                .callbackData(BotReplyPositionConstants.positionReturnToWalletListCli).build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId)
                .text(content.toString()).inlineKeyboardMarkup(markup)
                .build());

    }


    private String timeDiff(Timestamp time) {
        if(time == null) return "-";
        long now = TimestampUtil.now().getTime() / 1000;
        long diffInSeconds = now - time.getTime() / 1000;
        Long day = TimeUnit.SECONDS.toDays(diffInSeconds);
        Long hour = TimeUnit.SECONDS.toHours(diffInSeconds) % 24;
        Long minute = TimeUnit.SECONDS.toMinutes(diffInSeconds) % 60;
        return String.format("%sd %sh %smin", day, hour, minute);
    }

    @Resource
    private UserWalletService userWalletService;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private UserActionService userActionService;
    @Resource
    private WalletBalanceStatRepository walletBalanceStatRepository;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private TokenPxService tokenPxService;

}
