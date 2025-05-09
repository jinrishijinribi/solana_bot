package com.rich.sol_bot.bot;

import com.rich.sol_bot.bot.bots.SolLongPoolingBot;
import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.bot.bots.SolWebhookBot;
import com.rich.sol_bot.bot.queue.BotPushHandler;
import com.rich.sol_bot.bot.queue.message.BotMessageTypeEnum;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.function.Consumer;

@Service
@Slf4j
public class BotManager implements ApplicationRunner {

    private final TelegramBotsApi botsApi;
    @Value("${bot.update.mode.pooling:false}")
    private Boolean botModeIsPooling;
    public SolWebhookBot solWebhookBot;
    public SolLongPoolingBot solLongPoolingBot;
    public TokenBot bot;


    public BotManager() throws TelegramApiException {
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        DefaultBotSession defaultBotSession = new DefaultBotSession();
        defaultBotSession.setOptions(defaultBotOptions);
        this.botsApi = new TelegramBotsApi(defaultBotSession.getClass());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            String webhookUrl = systemConfigRepository.value(SystemConfigConstant.BOT_WEBHOOK_URL);
            if(botModeIsPooling) {
                solLongPoolingBot = new SolLongPoolingBot(
                        systemConfigRepository.value(SystemConfigConstant.BOT_TOKEN),
                        systemConfigRepository.value(SystemConfigConstant.BOT_NAME),
                        brc20BotMsgHandler,
                        botPushHandler
                );
                botsApi.registerBot(solLongPoolingBot);
                bot = solLongPoolingBot;
            } else {
                solWebhookBot = new SolWebhookBot(
                        systemConfigRepository.value(SystemConfigConstant.BOT_TOKEN),
                        systemConfigRepository.value(SystemConfigConstant.BOT_NAME),
                        webhookUrl,
                        brc20BotMsgHandler,
                        botPushHandler
                );
                botsApi.registerBot(solWebhookBot, new SetWebhook(webhookUrl));
                bot = solWebhookBot;
            }
        } catch (Exception e) {
            log.error("bot init error", e);
        }
    }

    public void pushMsg(Long uid, String context) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.simple_text).text(context.replaceAll("_", ""))
                .chatId(uid)
                .build());
    }

    public void pushContent(Long uid, Integer messageId, String content, InlineKeyboardMarkup markup, Consumer<Integer> consumer) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.inline_keyboard)
                .chatId(uid).messageId(messageId).text(content).inlineKeyboardMarkup(markup).consumer(consumer)
                .build());
    }

    public void pinMsg(Long uid, Integer messageId, Consumer<Integer> consumer) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.pin_message).messageId(messageId)
                .chatId(uid).consumer(consumer)
                .build());
    }

    public void unpinAll(Long uid, Consumer<Integer> consumer) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                .type(BotMessageTypeEnum.unpin_message)
                .chatId(uid).consumer(consumer)
                .build());
    }

    public void pushPhoto(Long uid, InputFile inputFile) {
        bot.sendMessageToQueue(BotPushMessage.builder()
                        .type(BotMessageTypeEnum.file)
                        .chatId(uid).inputFile(inputFile)
                .build());
    }

    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private BotMsgHandler brc20BotMsgHandler;
    @Resource
    @Lazy
    private BotPushHandler botPushHandler;
}
