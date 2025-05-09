package com.rich.sol_bot.sol.queue;

import com.rich.sol_bot.sniper.mapper.SniperPlanTx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wangqiyun
 * @since 2024/3/20 16:10
 */

@Service
@Slf4j
public class SolQueueService {

    private final LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    public boolean send(Message message) {
        try {
            log.info("send: {}", message.getSniper_plan_tx_id());
            queue.put(message);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Message receive() throws InterruptedException {
        return queue.take();
    }

    public boolean delete(Message message) {
        return true;
    }
}
