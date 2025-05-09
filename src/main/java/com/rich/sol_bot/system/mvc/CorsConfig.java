package com.rich.sol_bot.system.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author T.J
 * @date 2022/6/22 17:05
 */
@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${system.cors.origins}")
    private String[] origins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (origins == null || origins.length <= 0) {
            return;
        }
        registry.addMapping("/**")
                .allowedOriginPatterns(this.origins)
                .allowedMethods("*")
                .allowCredentials(true)
                .exposedHeaders("*")
                .maxAge(3600L);
        log.debug("完成跨域头设置: {}", Arrays.asList(origins));
    }
}
