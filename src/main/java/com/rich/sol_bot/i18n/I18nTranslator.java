package com.rich.sol_bot.i18n;

import cn.hutool.core.text.StrFormatter;
import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class I18nTranslator {
    @Resource
    private ObjectMapper objectMapper;

    public String getAndRenderContent(I18nLanguageEnum language, String content) {
//        Optional<String> result = getAndRenderTemplate(language, content, Lists.newArrayList());
//        return result.orElse("");
        return this.getContent(language, content);
    }

    public Optional<String> getAndRenderTemplate(I18nLanguageEnum language, String group, String template, List<String> args) {
        if (language == null) {
            return Optional.empty();
        }
        return language.getContent(template, group).map(t -> StrFormatter.format(t, args.toArray()));
    }


    public Optional<String> getAndRenderTemplate(I18nLanguageEnum language, String template, List<String> args) {
        return getAndRenderTemplate(language, "content", template, args);
    }

    public Map<String, String> map = new HashMap<>();

    public String getContent(I18nLanguageEnum language, String content) {
        String key = language.getValue() + content;
        String result = map.get(key);
        if(result != null) return result;
        Class<?> clazz = ZhCnValue.class;
        switch (language) {
            case zh_CN -> {
                clazz = ZhCnValue.class;
            }
            case en_US -> {
                clazz = EnUsValue.class;
            }
            case ko -> {
                clazz = KoValue.class;
            }
            case vi -> {
                clazz = ViValue.class;
            }
        }
        try {
            Field field = clazz.getDeclaredField(content);
            // 检查是否为静态字段
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                // 设置可访问
                field.setAccessible(true);
                // 获取静态字段的值
                result = field.get(null).toString(); // 静态字段，无需实例对象，传 null
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
