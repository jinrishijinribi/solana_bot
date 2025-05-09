package com.rich.sol_bot.bot.queue.message;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotMessageTypeEnum {
    file("file"),
    inline_keyboard("inline_keyboard"),
    reply_keyboard("reply_keyboard"),
    simple_text("simple_text"),
    answer_call_back_data("answer_call_back_data"),
    pin_message("pin_message"),
    unpin_message("unpin_message"),
    delete_message("delete_message")
    ;
    @EnumValue
    private final String value;
}
