package com.rich.sol_bot.bot.bots;

import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;


public interface TokenBot {
//    Integer pushContent(Long uid, Integer messageId, String content);
//    Integer pinMessage(Long uid, Integer messageId);
//    Integer unpinAll(Long uid);
//    Integer pushCallbackData(String callbackDataId, String content);
//    Integer pushContent(Long uid, Integer messageId, String content, InlineKeyboardMarkup markup);
//    Integer pushContentRaw(Long uid, Integer messageId, String content, InlineKeyboardMarkup markup);
//    Integer pushContent(Long uid, Integer messageId, String content, ReplyKeyboard markup);
//    Integer pushPhoto(Long uid, InputFile inputFile);
    String getBotUsername();
    Integer consumeMessage(BotPushMessage message);
    void sendMessageToQueue(BotPushMessage message);
    I18nLanguageEnum getLanguage(Long uid);
    void setLanguage(Long uid, I18nLanguageEnum language);

}
