package com.rich.sol_bot.bot;

import com.rich.sol_bot.bot.bots.TokenBot;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotMsgHandler {
    void handler(TokenBot bot, Update update);
}
