package com.rich.sol_bot.system.mvc;


import com.rich.sol_bot.system.common.RestControllerResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@RestControllerAdvice(basePackages = "com.rich.sol_bot")
public class GlobalResponseBodyConfig implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !converterType.equals(StringHttpMessageConverter.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response
    ) {
        if (body instanceof RestControllerResult) {
            return body;
        }
        return RestControllerResult.success(body);
    }

    public boolean isMatch(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof String || obj instanceof Boolean || obj instanceof Number;
    }
}
