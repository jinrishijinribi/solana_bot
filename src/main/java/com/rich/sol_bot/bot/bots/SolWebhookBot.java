package com.rich.sol_bot.bot.bots;

import com.rich.sol_bot.bot.BotMsgHandler;
import com.rich.sol_bot.bot.queue.BotPushHandler;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.system.enums.LanguageEnum;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.UnpinAllChatMessages;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SolWebhookBot extends TelegramWebhookBot implements TokenBot {
    private final String markdownFormat = "Markdown";
    private final BotMsgHandler handler;
    private final String botName;
    private final String webhookUrl;
    private final BotPushHandler botPushHandler;
    private final Map<Long, I18nLanguageEnum> languages = new HashMap<>();

    public SolWebhookBot(String botToken, String botName, String webhookUrl, BotMsgHandler handler, BotPushHandler botPushHandler) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
        this.webhookUrl = webhookUrl;
        this.botPushHandler = botPushHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            handler.handler(this, update);
        }catch (Exception e) {
            log.error("bot onWebhookUpdateReceived error", e);
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return "/api/bot/webhook";
    }

    @Override
    public Integer consumeMessage(BotPushMessage message) {
        Integer messageId = null;
        switch (message.getType()){
            case delete_message -> {
                messageId = this.deleteMessage(message.getChatId(), message.getMessageId());
            }
            case file -> {
                messageId = this.pushPhoto(message.getChatId(), message.getInputFile());
            }
            case pin_message -> {
                messageId = this.pinMessage(message.getChatId(), message.getMessageId());
            }
            case unpin_message -> {
                messageId = this.unpinAll(message.getChatId());
            }
            case inline_keyboard -> {
                if(message.getDisableMarkdown() != null && message.getDisableMarkdown()){
                    messageId = this.pushContentRaw(message.getChatId(), message.getMessageId(), message.getText(), message.getInlineKeyboardMarkup());
                } else {
                    messageId = this.pushContent(message.getChatId(), message.getMessageId(), message.getText(), message.getInlineKeyboardMarkup());
                }
            }
            case reply_keyboard -> {
                messageId = this.pushContent(message.getChatId(), message.getMessageId(), message.getText(), message.getReplyKeyboard());
            }
            case answer_call_back_data -> {
                messageId = this.pushCallbackData(message.getCallbackDataId(), message.getText());
            }
            case simple_text -> {
                messageId = this.pushContent(message.getChatId(), null, message.getText());
            }
        }
        log.info("consumeMessage, {}, {}", message.getChatId(), messageId);
        return messageId;
    }

    @Override
    public void sendMessageToQueue(BotPushMessage message){
        this.botPushHandler.send(message);
    }

    @Override
    public I18nLanguageEnum getLanguage(Long uid) {
        I18nLanguageEnum language = languages.get(uid);
        if(language == null) return I18nLanguageEnum.zh_CN;
        return language;
    }

    @Override
    public void setLanguage(Long uid, I18nLanguageEnum language) {
        languages.put(uid, language);
    }


    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }


//    @Override
    public Integer pushContent(Long uid, Integer messageId, String content) {
        try {
            if(messageId == null) {
                Message message = this.execute(SendMessage.builder().chatId(uid).parseMode(this.markdownFormat)
                        .text(content.replaceAll("_", "")).disableWebPagePreview(true)
                        .build());
                return message.getMessageId();
            } else {
                this.execute(EditMessageText.builder().chatId(uid).parseMode(this.markdownFormat).messageId(messageId)
                        .text(content.replaceAll("_", "")).disableWebPagePreview(true)
                        .build());
                return messageId;
            }
        }catch (Exception e) {
            log.error("bot pushContent error", e);
        }
        return 0;
    }

//    @Override
    public Integer pinMessage(Long uid, Integer messageId) {
        try {
            if(messageId != null) {
                this.execute(PinChatMessage.builder().chatId(uid).messageId(messageId)
                        .build());
            }
        }catch (Exception e) {
            log.error("bot pinMessage error", e);
        }
        return null;
    }

    public Integer deleteMessage(Long uid, Integer messageId) {
        try {
            if(messageId != null) {
                this.execute(DeleteMessage.builder()
                                .chatId(uid).messageId(messageId)
                        .build());
            }
        }catch (Exception e) {
            log.error("bot deleteMessage error", e);
        }
        return null;
    }

//    @Override
    public Integer unpinAll(Long uid) {
        try {
            this.execute(UnpinAllChatMessages.builder().chatId(uid).build());
        }catch (Exception e) {
            log.error("bot unpinAll error", e);
        }
        return 0;
    }
//    @Override
    public Integer pushCallbackData(String callbackDataId, String content) {
        try {
            this.execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackDataId).text(content)
                    .build());
        }catch (Exception e) {
            log.error("bot pushCallbackData error", e);
        }
        return null;
    }
//    @Override
    public Integer pushContent(Long uid, Integer messageId, String content, InlineKeyboardMarkup markup) {
        try {
            if(messageId == null) {
                Message message = this.execute(SendMessage.builder().chatId(uid).parseMode(this.markdownFormat)
                        .text(content).replyMarkup(markup)
                        .disableWebPagePreview(true)
                        .build());
                return message.getMessageId();
            } else {
                this.execute(EditMessageText.builder().chatId(uid).parseMode(this.markdownFormat).messageId(messageId)
                        .text(content).replyMarkup(markup)
                        .disableWebPagePreview(true)
                        .build());
                return messageId;
            }
        }catch (Exception e) {
            log.error("bot pushContent error", e);
        }
        return 0;
    }

//    @Override
    public Integer pushContentRaw(Long uid, Integer messageId, String content, InlineKeyboardMarkup markup) {
        try {
            if(messageId == null) {
                Message message = this.execute(SendMessage.builder().chatId(uid)
                        .text(content).replyMarkup(markup)
                        .disableWebPagePreview(true)
                        .build());
                return message.getMessageId();
            } else {
                this.execute(EditMessageText.builder().chatId(uid).messageId(messageId)
                        .text(content).replyMarkup(markup)
                        .disableWebPagePreview(true)
                        .build());
                return messageId;
            }
        }catch (Exception e) {
            log.error("bot pushContentRaw error", e);
        }
        return messageId;
    }

//    @Override
    public Integer pushContent(Long uid, Integer messageId, String content, ReplyKeyboard markup) {
        try {
            if(messageId == null) {
                Message message = this.execute(SendMessage.builder().chatId(uid).parseMode(this.markdownFormat)
                        .text(content).replyMarkup(markup)
                        .disableWebPagePreview(true)
                        .build());
                return message.getMessageId();
            }
        }catch (Exception e) {
            log.error("bot pushContent error", e);
        }
        return 0;
    }

//    @Override
    public Integer pushPhoto(Long uid, InputFile inputFile) {
        try {
            this.execute(SendPhoto.builder().chatId(uid)
                    .chatId(uid).photo(inputFile)
                    .build());
        }catch (Exception e) {
            log.error("bot pushPhoto error", e);
        }
        return null;
    }
}
