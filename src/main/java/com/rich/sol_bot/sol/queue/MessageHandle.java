package com.rich.sol_bot.sol.queue;

import com.rich.sol_bot.sniper.mapper.SniperPlanTx;
import com.rich.sol_bot.sol.SubmitTransactionService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangqiyun
 * @since 2024/3/20 17:12
 */


@Service
@Slf4j
public class MessageHandle {

    @PostConstruct
    public void init() {
        executorService.submit(() -> {
            while (true) {
                log.info("close: {}", close.get());
                if (close.get()) return;
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
                if (close.get()) return;
                Message message;
                try {
                    message = solQueueService.receive();
                } catch (Exception e) {
                    e.printStackTrace();
                    semaphore.release();
                    continue;
                }
                if (close.get()) return;
                executorService.submit(() -> {
                    try {
                        submitTransactionService.handle(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    semaphore.release();
                });
            }

        });
    }


    public final Semaphore semaphore = new Semaphore(5);
    private final ExecutorService executorService = Executors.newCachedThreadPool();


    @Resource
    private SubmitTransactionService submitTransactionService;
    @Resource
    private SolQueueService solQueueService;
    private final AtomicBoolean close = new AtomicBoolean(false);

    @PreDestroy
    public void destroy() {
        close.set(true);
    }
}
