package com.rich.sol_bot.system.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

;

/**
 * @author T.J
 * @date 2022/8/14 13:12
 */
public class RequestContextInterceptor implements HandlerInterceptor {
    private final RequestContextHolder requestContextHolder;

    public RequestContextInterceptor(RequestContextHolder requestContextHolder) {
        this.requestContextHolder = requestContextHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        requestContextHolder.init(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 不要忘记清理
        requestContextHolder.clear();
    }
}
