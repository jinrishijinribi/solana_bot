package com.rich.sol_bot.bot.queue.message;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.Consumer;

@Data
@Builder
public class BotPushMessage {
    private BotMessageTypeEnum type;
    private Long chatId;
    private Integer messageId;
    private String text;
    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private ReplyKeyboard replyKeyboard;
    private InputFile inputFile;
    private String callbackDataId;
    private Boolean disableMarkdown;
    private Consumer<Integer> consumer;
}
