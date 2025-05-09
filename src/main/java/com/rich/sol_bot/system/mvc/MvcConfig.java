package com.rich.sol_bot.system.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author T.J
 * @date 2023/2/20 09:48
 */
@Slf4j
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public RequestContextHolder requestContextHolder() {
        return new RequestContextHolder();
    }

    @Bean
    public RequestContextInterceptor requestContextInterceptor() {
        return new RequestContextInterceptor(requestContextHolder());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestContextInterceptor())
                .addPathPatterns("/api/**");
        log.info("配置MVC请求拦截器, 拦截接口 [{}]", "/api/**");
    }
}
