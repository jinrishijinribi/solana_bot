package com.rich.sol_bot.user.enums;

import cn.hutool.setting.Setting;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// 中文 英语 韩语 越南语
@Getter
@AllArgsConstructor
public enum I18nLanguageEnum {
    zh_CN("zh_CN", "i18n/zh_CN.setting"),
    en_US("en_US", "i18n/en_US.setting"),
    ko("ko", "i18n/ko.setting"),
    vi("vi", "i18n/vi.setting"),
    test("test", "i18n/test.json")
    ;
    @EnumValue
    private final String value;
    private final String path;


    public Optional<String> getContent(String prop) {
        return getContent(prop, "content");
    }

    public Optional<String> getContent(String prop, String group) {
        return Optional.ofNullable(getSettings().getByGroup(prop, group));
    }

    public String getContentFromJson(String str) {
        return getJson().get(str);
    }

    public Setting getSettings() {
        Setting setting = INSTANT.get(this);
        if (setting == null) {
            synchronized (this) {
                setting = INSTANT.get(this);
                if (setting == null) {
                    setting = new Setting(getPath());
                    INSTANT.put(this, setting);
                }
            }
        }
        return setting;
    }

    public Map<String, String> getJson() {
        // 创建 ObjectMapper 实例
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 读取文件内容为字符串
            String content = new String(Files.readAllBytes(Paths.get(getPath())));

            // 将 JSON 字符串转换为 Map
            Map<String, String> jsonMap = objectMapper.readValue(content, new TypeReference<Map<String, String>>() {});

            // 打印 Map 内容
            return jsonMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static final EnumMap<I18nLanguageEnum, Setting> INSTANT = new EnumMap<>(I18nLanguageEnum.class);

}
