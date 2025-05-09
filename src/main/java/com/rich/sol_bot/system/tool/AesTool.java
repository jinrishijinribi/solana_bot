package com.rich.sol_bot.system.tool;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author T.J
 * @date 2021/12/8 11:38
 */
@Slf4j
@Component
public class AesTool {
    private final GenericObjectPool<AES> pool;

    public AesTool(@Value("${SYSTEM_SECRET_AES}") String key) {
        GenericObjectPoolConfig<AES> poolConfig = new GenericObjectPoolConfig<>();
        // 最大对象数量，包含借出去的和空闲的
        poolConfig.setMaxTotal(100);
        // 对象池满了，是否阻塞获取（false则借不到直接抛异常）
        poolConfig.setBlockWhenExhausted(true);
        // 创建对象池
        this.pool = new GenericObjectPool<>(new AesCodecFactory(key), poolConfig);
    }

    public String encrypt(String raw) {
        AES aes = null;
        try {
            // 借出对象
            aes = pool.borrowObject();
            return aes.encryptHex(raw);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            if (aes != null) {
                // 归还对象
                pool.returnObject(aes);
            }
        }
    }

    public String decrypt(String encryptContent) {
        AES aes = null;
        try {
            // 借出对象
            aes = pool.borrowObject();
            return aes.decryptStr(encryptContent);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            if (aes != null) {
                // 归还对象
                pool.returnObject(aes);
            }
        }
    }


    @Slf4j
    public static class AesCodecFactory extends BasePooledObjectFactory<AES> {
        private final String key;
        private final AtomicInteger counter = new AtomicInteger(0);

        public AesCodecFactory(String key) {
            this.key = key;
        }

        @Override
        public AES create() throws Exception {
            AES codec = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
            int total = counter.incrementAndGet();
            log.info("创建新的AES编解码器, total={}", total);
            return codec;
        }

        @Override
        public PooledObject<AES> wrap(AES aes) {
            return new DefaultPooledObject<>(aes);
        }
    }
}
