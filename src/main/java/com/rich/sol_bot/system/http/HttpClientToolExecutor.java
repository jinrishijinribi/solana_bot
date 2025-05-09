package com.rich.sol_bot.system.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rich.sol_bot.system.exception.DefaultAppExceptionCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;


/**
 * @author T.J
 * @date 2022/9/14 14:01
 */
@Component
public class HttpClientToolExecutor {
    @Resource
    private ObjectMapper objectMapper;

    private String getHttpClientResult(Supplier<HttpClientTool.HttpClientResult> httpClientResultSupplier) {
        HttpClientTool.HttpClientResult httpClientResult = httpClientResultSupplier.get();
        if (!httpClientResult.isSuccess()) {
            throw DefaultAppExceptionCode.HTTP_REQUEST_ERROR.toException();
        }
        return httpClientResult.getContent();
    }

    public <T> T execute(Supplier<HttpClientTool.HttpClientResult> httpClientResultSupplier, Class<T> tClass) {
        String content = getHttpClientResult(httpClientResultSupplier);
        try {
            return objectMapper.readValue(content, tClass);
        } catch (Exception exception) {
            throw DefaultAppExceptionCode.HTTP_REQUEST_ERROR.toException();
        }
    }

    public <T> T execute(Supplier<HttpClientTool.HttpClientResult> httpClientResultSupplier,
                         TypeReference<T> typeReference) {
        String content = getHttpClientResult(httpClientResultSupplier);
        try {
            return objectMapper.readValue(content, typeReference);
        } catch (Exception exception) {
            throw DefaultAppExceptionCode.HTTP_REQUEST_ERROR.toException();
        }
    }
}
