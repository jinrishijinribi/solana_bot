package com.rich.sol_bot.system.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class RedisKeyGenerateTool {
    @Value("${spring.application.name}")
    private String appName;

    public static final String DELIMITER = ":";

    public String generateCommonKey(String... name) {
//        return (String.join(DELIMITER,name)).toLowerCase();
        return (appName + DELIMITER + String.join(DELIMITER,name)).toLowerCase();
    }

    public String generateName(String... name) {
        return (appName + DELIMITER + String.join(DELIMITER,name)).toLowerCase();
    }

    public String generateKey(String... name) {
        return (appName + DELIMITER + String.join(DELIMITER,name)).toLowerCase();
    }
}
