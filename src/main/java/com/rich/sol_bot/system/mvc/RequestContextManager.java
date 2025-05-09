package com.rich.sol_bot.system.mvc;

import com.rich.sol_bot.admin.mapper.Admin;
import com.rich.sol_bot.admin.service.AdminLoginService;
import com.rich.sol_bot.system.enums.LanguageEnum;
import com.rich.sol_bot.system.exception.DefaultAppExceptionCode;
//import com.rich.sol_bot.user_token.UserTokenService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Optional;


@Component
public class RequestContextManager {
    private final RequestContextHolder requestContextHolder;
//    private final UserLoginAppService userLoginAppService;

    public RequestContextManager(RequestContextHolder requestContextHolder) {
        this.requestContextHolder = requestContextHolder;
//        this.userLoginAppService = userLoginAppService;
    }

    public RequestContext getContext() {
        return requestContextHolder.getContext();
    }

    public String token() {
        RequestContext requestContext = requestContextHolder.getContext();
        return requestContext.getToken();
    }

    public Timestamp now() {
        RequestContext requestContext = requestContextHolder.getContext();
        return requestContext.getNow();
    }

    public LanguageEnum language() {
        RequestContext requestContext = requestContextHolder.getContext();
        return requestContext.getLanguage();
    }

    public String requestId() {
        RequestContext requestContext = requestContextHolder.getContext();
        return requestContext.getRequestId();
    }

    public Optional<Long> uid() {
//        String token = token();
//        if (StringUtils.isBlank(token)) {
//            return Optional.empty();
//        }
//        Long uid = userTokenService.getIdByToken(token);
//        if (uid == null || uid <= 0L) {
//            return Optional.empty();
//        }
//        return Optional.of(uid);
        return Optional.of(0L);
    }

    public long uidOrElseThrow() {
//        String token = token();
//        if (StringUtils.isBlank(token)) {
//            throw DefaultAppExceptionCode.UN_LOGIN.toException();
//        }
//        Long uid = userTokenService.getIdByToken(token);
//        if (uid == null || uid <= 0L) {
//            throw DefaultAppExceptionCode.UN_LOGIN.toException();
//        }
//        return uid;
        return 0;
    }

    public Admin admin() {
        String token = token();
        return adminLoginService.getCacheAdmin(token);
    }
//
//    public Boolean adminPower() {
//        return AdminType.admin.equals(admin().getType());
//    }

    public String ip() {
        return requestContextHolder.getContext().getIp();
    }

    public String botSecretToken() {
        return requestContextHolder.getContext().getBotSecretToken();
    }

//    @Resource
//    private UserTokenService userTokenService;
//    @Resource
//    private AdminRepository adminRepository;

    @Resource
    private AdminLoginService adminLoginService;
}
