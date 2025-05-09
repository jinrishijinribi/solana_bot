package com.rich.sol_bot.system.mvc;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author T.J
 * @date 2021/12/28 15:22
 */
@Slf4j
public class RequestContextHolder implements AutoClearThreadLocal {
    private final ThreadLocal<RequestContext> REQUEST_CONTEXT = new ThreadLocal<>();

    public void init(HttpServletRequest request) {
        REQUEST_CONTEXT.set(RequestContext.ofHttpServletRequest(request));
    }

    public RequestContext getContext() {
        return REQUEST_CONTEXT.get();
    }

    @Override
    public void clear() {
        REQUEST_CONTEXT.remove();
    }
}
