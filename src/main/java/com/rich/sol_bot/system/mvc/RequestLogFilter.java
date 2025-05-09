package com.rich.sol_bot.system.mvc;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rich.sol_bot.system.common.IpUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebFilter(urlPatterns = {"/api/*"}, filterName = "requestLogFilter")
public class RequestLogFilter implements Filter {
    private final ObjectMapper objectMapper;

    public RequestLogFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        log.info("创建 RequestLogFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startAt = System.currentTimeMillis();
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        byte[] requestByte = httpServletRequest.getContentAsByteArray();
        try {
            String requestBody = "";
            String parameter = "";
            if (requestByte.length == 0) {
                parameter = objectMapper.writeValueAsString(httpServletRequest.getParameterMap());
            } else {
                requestBody = StrUtil.str(requestByte, StandardCharsets.UTF_8);
            }
            String responseBody = "";
            String responseContentType = httpServletResponse.getContentType();
            if (responseContentType != null) {
                if (responseContentType.contains("image")) {
                    // 图片信息
                    requestBody = responseContentType;
                } else {
                    responseBody = StrUtil.str(httpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
                }
            }
            Map<String, Object> jsonLog = new HashMap<>(16);
            jsonLog.put("header0", getHeaders(httpServletRequest));
            jsonLog.put("body0", requestBody);
            jsonLog.put("parameter", parameter);
            jsonLog.put("method", httpServletRequest.getMethod());
            jsonLog.put("path", httpServletRequest.getRequestURI());
            jsonLog.put("status", httpServletResponse.getStatus());
            jsonLog.put("body1", responseBody);
            jsonLog.put("ip", IpUtil.getIp(httpServletRequest));
            jsonLog.put("cost", System.currentTimeMillis() - startAt);
            jsonLog.put("header1", getHeaders(httpServletResponse));
            log.info(objectMapper.writeValueAsString(jsonLog));
        } catch (Exception e) {
            log.error("请求日志记录器异常", e);
            throw e;
        } finally {
            httpServletResponse.copyBodyToResponse();
        }
    }

    public static Map<String, String> getHeaders(ContentCachingRequestWrapper requestWrapper) {
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>(24);
        headerNames.asIterator().forEachRemaining(header -> headerMap.put(header, requestWrapper.getHeader(header)));
        return headerMap;
    }

    public static Map<String, String> getHeaders(ContentCachingResponseWrapper responseWrapper) {
        Collection<String> headerNames = responseWrapper.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>(headerNames.size());
        for (final String headerName : headerNames) {
            headerMap.put(headerName, responseWrapper.getHeader(headerName));
        }
        return headerMap;
    }
}
