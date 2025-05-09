package com.rich.sol_bot.system.exception;

import com.rich.sol_bot.system.common.RestControllerResult;
import com.rich.sol_bot.system.enums.LanguageEnum;
import com.rich.sol_bot.system.mvc.RequestContextManager;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerConfig {

    private final boolean printAppExceptionStackTrace;

    public GlobalExceptionHandlerConfig(
            @Value("${PRINT_APP_EXCEPTION_STACK_TRACE:false}") boolean printAppExceptionStackTrace
    ) {
        log.info("配置全局错误处理GlobalExceptionHandler");
        this.printAppExceptionStackTrace = printAppExceptionStackTrace;
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            BeanInitializationException.class,
            BindException.class,
            ServerWebInputException.class,
    })
    public RestControllerResult handlerNotValidException(Exception exception, HttpServletResponse response) {
        String errorMsg;
        if (exception instanceof MethodArgumentNotValidException) {
            final BindingResult bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            errorMsg = extractErrorMsg(bindingResult);
        } else if (exception instanceof final BindingResult bindingResult) {
            errorMsg = extractErrorMsg(bindingResult);
        } else if (exception instanceof ServerWebInputException serverWebInputException) {
            errorMsg = serverWebInputException.getReason();
        } else {
            errorMsg = exception.getMessage();
        }
        String code = DefaultAppExceptionCode.PARAM_ERROR.errorCode();
        int httpStatusCode = DefaultAppExceptionCode.PARAM_ERROR.getHttpCode();
        response.setStatus(httpStatusCode);
        return new RestControllerResult(code, errorMsg, null);
    }

    private static String extractErrorMsg(BindingResult bindingResult) {
        final List<ObjectError> allErrors = bindingResult.getAllErrors();
        return allErrors.stream().map(item -> {
            if (item instanceof FieldError) {
                String field = ((FieldError) item).getField();
                return field + ":" + item.getDefaultMessage();
            } else {
                return item.getDefaultMessage();
            }
        }).collect(Collectors.joining(";"));
    }

    /**
     * 业务异常
     *
     * @param exception 异常
     * @param response res
     * @return 信息
     */
    @ExceptionHandler(AppException.class)
    public RestControllerResult handlerAppException(AppException exception, HttpServletResponse response) {
        AppExceptionCodeTranslate codeTranslate = exception.getCodeTranslate();
        LanguageEnum language = requestContextManager.language();
        String errMsg = codeTranslate.getUserFacedMsg();
        switch (language){
            case zh -> {
                errMsg = codeTranslate.getZhMsg();
            }
            case en -> {
                errMsg = codeTranslate.getUserFacedMsg();
            }
        }
        if (exception.isPrintThrow()) {
            log.error("AppException", exception);
        } else {
            // 系统配置层面是否打印错误堆栈
            if (printAppExceptionStackTrace) {
                log.error("AppException", exception);
            } else {
                log.warn("AppException: {}", codeTranslate.name());
            }
        }
        response.setStatus(codeTranslate.getHttpCode());
        return new RestControllerResult(
                codeTranslate.errorCode(),
                errMsg,
                null
        );
    }


    /**
     * 防止SQL异常message泄露给的外部
     *
     * @param exception IbatisException异常
     * @return 所务信息
     */
    @ExceptionHandler(PersistenceException.class)
    public RestControllerResult handleIbatisException(PersistenceException exception, HttpServletResponse response) {
        log.error("异常SQL", exception);
        response.setStatus(DefaultAppExceptionCode.DATABASE_ERROR.getHttpCode());
        return new RestControllerResult(
                DefaultAppExceptionCode.DATABASE_ERROR.errorCode(),
                DefaultAppExceptionCode.DATABASE_ERROR.getUserFacedMsg(),
                null
        );
    }

    /**
     * 兜底异常,系统异常
     *
     * @param exception 异常
     * @param response res
     * @return 响应
     */
    @ExceptionHandler(Exception.class)
    public RestControllerResult handlerException(Exception exception, HttpServletResponse response) {
        final String message = exception.getMessage();
        log.error(message, exception);
        response.setStatus(DefaultAppExceptionCode.SYSTEM_ERROR.getHttpCode());
        return new RestControllerResult(
                DefaultAppExceptionCode.SYSTEM_ERROR.errorCode(),
                DefaultAppExceptionCode.SYSTEM_ERROR.getUserFacedMsg(),
                null
        );
    }

    @Resource
    private RequestContextManager requestContextManager;
}
