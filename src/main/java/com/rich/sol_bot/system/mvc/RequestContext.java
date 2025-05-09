package com.rich.sol_bot.system.mvc;

import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.IpUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.enums.LanguageEnum;
import com.rich.sol_bot.system.exception.DefaultAppExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

@Value
@Builder
public class RequestContext {
    public static final String TOKEN_SYMBOL = "token";
    public static final String DEVICE_SYMBOL = "";
    public static final String VERSION_SYMBOL = "";
    public static final String LANGUAGE_SYMBOL = "language";
    public static final String BOT_SECRET_TOKEN = "x-telegram-bot-api-secret-token";


    String token;
    String device;
    String ip;
    String version;
    Timestamp now;
    String requestId;
    LanguageEnum language;
    String botSecretToken;

    public static RequestContext ofHttpServletRequest(HttpServletRequest request) {
        String ip = IpUtil.getIp(request);
        String token = request.getHeader(TOKEN_SYMBOL);
        if (StringUtils.length(token) > 1000) {
            throw DefaultAppExceptionCode.SYSTEM_ERROR.toException();
        }
        String device = request.getHeader(DEVICE_SYMBOL);
        if (StringUtils.length(device) > 1000) {
            throw DefaultAppExceptionCode.SYSTEM_ERROR.toException();
        }
        String version = request.getHeader(VERSION_SYMBOL);
        if (StringUtils.length(version) > 1000) {
            throw DefaultAppExceptionCode.SYSTEM_ERROR.toException();
        }
        String languageStr = request.getHeader(LANGUAGE_SYMBOL);
        LanguageEnum language = LanguageEnum.en;
        if(StringUtils.isNotBlank(languageStr)){
            language = LanguageEnum.valueOf(languageStr);
        }
        String botSecretToken = request.getHeader(BOT_SECRET_TOKEN);
        return builder()
                .ip(ip)
                .token(token)
                .device(device)
                .version(version)
                .now(TimestampUtil.now())
                .requestId(IdUtil.uuid())
                .language(language)
                .botSecretToken(botSecretToken)
                .build();
    }

}
