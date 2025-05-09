package com.rich.sol_bot.bot.queue;

import com.rich.sol_bot.bot.BotManager;
import com.rich.sol_bot.bot.queue.message.BotPushMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class BotPushHandler {
    private final LinkedBlockingQueue<BotPushMessage> queue = new LinkedBlockingQueue<>();
    public boolean send(BotPushMessage message) {
        try {
            queue.put(message);
            return true;
        } catch (InterruptedException e) {
            log.error("bot push queue error", e);
            return false;
        }
    }

    @PostConstruct
    public void init() {
        executorService.submit(() -> {
            while (true) {
                if (close.get()) return;
                BotPushMessage message;
                try {
                    message = queue.take();
                } catch (Exception e) {
                    log.error("bot push queue error", e);
                    continue;
                }
                executorService.submit(() -> {
                    try {
                        Integer messageId = botManager.bot.consumeMessage(message);
                        if(message.getConsumer() != null && messageId != null) {
                            message.getConsumer().accept(messageId);
                        }
                    } catch (Exception e) {
                        log.error("bot push queue consume error", e);
                    }
                });
            }
        });
    }
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final AtomicBoolean close = new AtomicBoolean(false);

    @PreDestroy
    public void destroy() {
        close.set(true);
    }


    @Resource
    private BotManager botManager;
}
