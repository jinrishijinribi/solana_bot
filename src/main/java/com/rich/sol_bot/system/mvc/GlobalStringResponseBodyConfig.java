package com.rich.sol_bot.system.mvc;

import com.rich.sol_bot.system.common.RestControllerResult;
import com.rich.sol_bot.system.json.JacksonOperator;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.rich.sol_bot")
public class GlobalStringResponseBodyConfig implements ResponseBodyAdvice<Object> {
    private final JacksonOperator jacksonOperator;

    public GlobalStringResponseBodyConfig(JacksonOperator jacksonOperator) {
        this.jacksonOperator = jacksonOperator;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return converterType.equals(StringHttpMessageConverter.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        RestControllerResult restControllerResult = RestControllerResult.success(body);
        HttpHeaders headers = response.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return jacksonOperator.writeValueAsString(restControllerResult);
    }
}
