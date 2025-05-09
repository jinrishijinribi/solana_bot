package com.rich.sol_bot.bot.handler;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.handler.constants.BotReplyAllConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyDealConstants;
import com.rich.sol_bot.bot.handler.constants.BotReplyGenerateService;
import com.rich.sol_bot.bot.handler.constants.BotReplyGroupConstants;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class BotGroupHandler {
    @Autowired
    private I18nTranslator i18nTranslator;

    public void showGroupBoard(TokenBot bot, Long chatId, String ca) {
        TokenBaseInfo tokenBaseInfo = tokenInfoService.geneTokenBaseInfo(ca);
        String content = String.format(i18nTranslator.getAndRenderContent(I18nLanguageEnum.zh_CN, BotReplyAllConstants.dealSellTextPrefix),
                tokenBaseInfo.getSymbol(),
                tokenBaseInfo.getAddress()
        );
        content += botReplyGenerateService.generateTokenStaticHead(tokenBaseInfo, I18nLanguageEnum.zh_CN);
//        content += botReplyGenerateService.generateTokenGroup(tokenBaseInfo);
        content += botReplyGenerateService.generateTokenStaticSocial(tokenBaseInfo, I18nLanguageEnum.zh_CN);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        String botName = bot.getBotUsername();
        keyboard.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(BotReplyGroupConstants.groupFastTradeText)
//                                .callbackData(BotReplyGroupConstants.groupFastTradeText)
                                .url("https://t.me/" + botName + "?start=" + ca)
                                .build()
                )
        );
        markup.setKeyboard(keyboard);
        bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.inline_keyboard)
                        .chatId(chatId)
                        .text(content).inlineKeyboardMarkup(markup)
                .build());
    }


    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private BotReplyGenerateService botReplyGenerateService;
}
