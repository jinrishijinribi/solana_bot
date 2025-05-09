package com.rich.sol_bot.system.common;

import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.core.thread.ThreadUtil;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * @author T.J
 * @date 2023/7/18 18:41
 */
@Slf4j
public class ThreadAsyncUtil {
    private static final ThreadLocal<String> CTX = new ThreadLocal<>();

    private ThreadAsyncUtil() {
    }

    public static Future<?> execAsync(Runnable runnable) {
        return execAsync(runnable, false);
    }

    public static Future<?> execAsync(Runnable runnable, boolean printLog) {
        String traceId = null;
        if (printLog) {
            traceId = getTraceId();
            if (StringUtils.isBlank(traceId)) {
                traceId = IdUtil.uuid();
                setTraceId(traceId);
            }
        }
        try {
            return execAsync(runnable, traceId, printLog);
        } finally {
            removeTraceId();
        }
    }


    /**
     * 使用异步线程执行
     *
     * @param runnable 执行任务
     * @param uniqId   唯一ID方便日志排查（可选）,推荐传入该值
     */
    public static Future<?> execAsync(Runnable runnable, @Nullable String uniqId, boolean printLog) {
        String callerThreadName;
        if (printLog) {
            callerThreadName = Thread.currentThread().getName();
            log.info("send async ops [{}, {}]", callerThreadName, uniqId);
        } else {
            callerThreadName = null;
        }
        return ThreadUtil.execAsync(() -> {
            if (printLog && StringUtils.isNotBlank(uniqId)) {
                setTraceId(uniqId);
            }
            if (printLog) {
                String executorThreadName = Thread.currentThread().getName();
                log.info("recv async ops [{}, {}, {}]", callerThreadName, executorThreadName, uniqId);
            }
            try {
                runnable.run();
            } catch (Exception exception) {
                log.error("execute async error", exception);
            } finally {
                removeTraceId();
            }
        });
    }

    public static Executor getExecutor() {
        return GlobalThreadPool.getExecutor();
    }


    public static String getTraceId() {
        return CTX.get();
    }

    public static void setTraceId(String traceId) {
        CTX.set(traceId);
    }

    public static void removeTraceId() {
        CTX.remove();
    }
}
