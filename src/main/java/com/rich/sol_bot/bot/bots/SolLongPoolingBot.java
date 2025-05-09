package com.rich.sol_bot.bot.bots;

import com.rich.sol_bot.bot.BotMsgHandler;
import com.rich.sol_bot.bot.queue.BotPushHandler;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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
import java.util.List;
import java.util.Map;

@Slf4j
public class SolLongPoolingBot extends TelegramLongPollingBot implements TokenBot {
    private final String markdownFormat = "Markdown";
    private final BotMsgHandler handler;
    private final String botName;
    private final BotPushHandler botPushHandler;
    private final Map<Long, I18nLanguageEnum> languages = new HashMap<>();
    public SolLongPoolingBot(String botToken, String botName, BotMsgHandler handler, BotPushHandler botPushHandler) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
        this.botPushHandler = botPushHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
        for(Update u: updates) {
            try {
                handler.handler(this, u);
            } catch (Exception e) {
                log.error("bot update error", e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public void onRegister() {
        super.onRegister();
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
    public Integer consumeMessage(BotPushMessage message) {
        switch (message.getType()){
            case delete_message -> {
                return this.deleteMessage(message.getChatId(), message.getMessageId());
            }
            case file -> {
                return this.pushPhoto(message.getChatId(), message.getInputFile());
            }
            case pin_message -> {
                return this.pinMessage(message.getChatId(), message.getMessageId());
            }
            case unpin_message -> {
                return this.unpinAll(message.getChatId());
            }
            case inline_keyboard -> {
                if(message.getDisableMarkdown() != null && message.getDisableMarkdown()){
                    return this.pushContentRaw(message.getChatId(), message.getMessageId(), message.getText(), message.getInlineKeyboardMarkup());
                } else {
                    return this.pushContent(message.getChatId(), message.getMessageId(), message.getText(), message.getInlineKeyboardMarkup());
                }
            }
            case reply_keyboard -> {
                return this.pushContent(message.getChatId(), message.getMessageId(), message.getText(), message.getReplyKeyboard());
            }
            case answer_call_back_data -> {
                return this.pushCallbackData(message.getCallbackDataId(), message.getText());
            }
            case simple_text -> {
                return this.pushContent(message.getChatId(), null, message.getText());
            }
        }
        return null;
    }

    @Override
    public void sendMessageToQueue(BotPushMessage message){
        botPushHandler.send(message);
    }

//    @Override
    public Integer pushContent(Long uid, Integer messageId, String content) {
        try {
            if(messageId == null) {
                Message message = this.execute(SendMessage.builder().chatId(uid).parseMode(this.markdownFormat)
                        .text(content).disableWebPagePreview(true)
                        .build());
                return message.getMessageId();
            } else {
                this.execute(EditMessageText.builder().chatId(uid).parseMode(this.markdownFormat).messageId(messageId)
                        .text(content).disableWebPagePreview(true)
                        .build());
                return messageId;
            }
        }catch (Exception e) {
            log.error("bot pushContent error", e);
        }
        return messageId;
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
        return messageId;
    }

//    @Override
    public Integer unpinAll(Long uid) {
        try {
            Boolean success = this.execute(UnpinAllChatMessages.builder().chatId(uid).build());
        }catch (Exception e) {
            log.error("bot unpinAll error", e);
        }
        return 0;
    }
//    @Override
    public Integer pushCallbackData(String callbackDataId, String content) {
        try {
            Boolean success = this.execute(AnswerCallbackQuery.builder()
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
//            this.pushError(uid);
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
    public Integer pushContentRaw(Long uid, Integer messageId, String content, InlineKeyboardMarkup markup) {
        try {
            if(messageId == null) {
                Message message = this.execute(SendMessage.builder().chatId(uid)
                        .text(content).replyMarkup(markup)
                        .disableWebPagePreview(true)
                        .build());
//                return message.getMessageId();
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
//            this.pushError(uid);
        }
        return null;
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
            return messageId;
        }catch (Exception e) {
            log.error("bot pushContent error", e);
//            this.pushError(uid);
        }
        return null;
    }

//    @Override
    public Integer pushPhoto(Long uid, InputFile inputFile) {
        try {
            Message message = this.execute(SendPhoto.builder().chatId(uid)
                    .chatId(uid).photo(inputFile)
                    .build());
            return message.getMessageId();
        }catch (Exception e) {
            log.error("bot pushPhoto error", e);
        }
        return null;
    }
}
