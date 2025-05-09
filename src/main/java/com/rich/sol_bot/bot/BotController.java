package com.rich.sol_bot.bot;

import com.rich.sol_bot.bot.bots.SolWebhookBot;
import com.rich.sol_bot.system.config.SystemConfigConstant;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.system.mvc.RequestContextManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@Tag(name = "bot")
@RestController
@RequestMapping("/api/bot")
public class BotController implements ApplicationRunner {

    private String selfSecretToken = null;

    @Operation(summary = "回调")
    @PostMapping("/webhook")
    public String webhook(@RequestBody Update update) {
        String secretToken = requestContextManager.botSecretToken();
        if(!selfSecretToken.equals(secretToken)) return "success";
        SolWebhookBot bot = botManager.solWebhookBot;
        bot.onWebhookUpdateReceived(update);
        return "success";
    }


    @Resource
    private BotManager botManager;
    @Resource
    private RequestContextManager requestContextManager;
    @Resource
    private SystemConfigRepository systemConfigRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        selfSecretToken = systemConfigRepository.value(SystemConfigConstant.BOT_WEBHOOK_SECRET_TOKEN);
    }
}
