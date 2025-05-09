package com.rich.sol_bot.twitter;

import cn.hutool.core.util.URLUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.rich.sol_bot.system.json.JacksonOperator;
import feign.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class TwitterClientInterceptor implements RequestInterceptor, ResponseInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {

        Map<String, Collection<String>> headers = new HashMap<>(0);
        headers.put("Content-Type", List.of("application/json"));
        requestTemplate.headers(headers);
        String method = requestTemplate.method();
        String path = requestTemplate.path();
        Map<String, Object> requestData = jacksonOperator.readValue(new String(requestTemplate.body()), new TypeReference<Map<String, Object>>() {
        });
        String bodyContent = jacksonOperator.writeValueAsString(requestData);
        // 重组请求
        // 先重置请求 body
        requestTemplate.body(null, StandardCharsets.UTF_8);
        if (method.equals(Request.HttpMethod.GET.name())) {
            // 清空 body,GET 请求将参数拼接在 url 上
            if (requestData != null && !requestData.isEmpty()) {
                path += "?" + URLUtil.buildQuery(requestData, StandardCharsets.UTF_8);
                requestTemplate.uri(path);
            }
            bodyContent = "";
        } else {
            requestTemplate.body(bodyContent);
        }
    }

    @Override
    public Object aroundDecode(InvocationContext invocationContext) throws IOException {
        return invocationContext.proceed();
    }

    @Resource
    private JacksonOperator jacksonOperator;

}
