package com.qstarter.admin.config;

import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import com.qstarter.core.model.GenericErrorMessage;
import com.qstarter.security.config.SystemWebResponseExceptionTranslator;
import com.qstarter.security.exception.UnAuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-09-06 10:59
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final Environment env;

    public GlobalExceptionHandler(Environment env) {
        this.env = env;
    }

    @ExceptionHandler({SystemServiceException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            Exception.class})
    public ResponseEntity<GenericMsg<?>> handleCustomServiceException(Exception ex) {

        GenericMsg<Void> fail;

        if (ex instanceof SystemServiceException) {

            //自定义异常处理
            SystemServiceException e = (SystemServiceException) ex;
            fail = GenericMsg.fail(e);

        } else if (ex instanceof ConstraintViolationException) {

            //@RequestParam 请求参数的验证 异常处理
            ConstraintViolationException e = (ConstraintViolationException) ex;
            fail = GenericMsg.fail(ErrorMessageEnum.CONSTRAINT_VIOLATION_EXCEPTION, e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));

        } else if (ex instanceof MethodArgumentNotValidException) {

            //@RequestBody 参数验证
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            String errMsg = e.getBindingResult().getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(","));
            fail = GenericMsg.fail(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, errMsg);

        } else if (ex instanceof HttpMediaTypeNotSupportedException) {

            HttpMediaTypeNotSupportedException e = (HttpMediaTypeNotSupportedException) ex;
            fail = GenericMsg.fail(ErrorMessageEnum.HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION);

        } else if (ex instanceof HttpMessageNotReadableException) {
            fail = GenericMsg.fail(ErrorMessageEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "未收到参数或参数类型错误。" + ((HttpMessageNotReadableException) ex).getMessage());
        } else if (ex instanceof MaxUploadSizeExceededException) {
            String property = env.getProperty("spring.servlet.multipart.max-request-size");
            fail = GenericMsg.fail(ErrorMessageEnum.FILE_SIZE_OVERSIZE, "上传的文件过大，最大只能上传：" + property + "文件");
        } else {
            log.error(ex.getMessage(), ex);
            fail = GenericMsg.fail(ErrorMessageEnum.SYSTEM_UNKNOWN_ERROR);
        }

        return ResponseEntity.ok(fail);
    }

    @ExceptionHandler
    public ResponseEntity<GenericErrorMessage> handlerAccessDeniedException(AccessDeniedException ex) {
        SystemWebResponseExceptionTranslator translator = new SystemWebResponseExceptionTranslator();
        try {
            return translator.translate(ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GenericMsg.fail(ErrorMessageEnum.SYSTEM_UNKNOWN_ERROR));
        }
    }

    //请求方法不支持
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public GenericMsg<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return GenericMsg.fail(ErrorMessageEnum.METHOD_NOT_ALLOWED, ex.getMethod() + "请求不被支持");
    }

    //缺少请求参数
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericMsg<Void> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {

        return GenericMsg.fail(ErrorMessageEnum.BAD_REQUEST, "缺少:" + ex.getParameterName() + " 参数");
    }


    //缺少请求参数
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericMsg<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        String requireType = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        return GenericMsg.fail(ErrorMessageEnum.BAD_REQUEST, " 参数:" + parameterName + "要求【" + requireType + "】。");
    }


    //缺少权限
    @ExceptionHandler(UnAuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public GenericMsg unAuthorizationException(UnAuthorizationException ex) {
        return GenericMsg.fail(ErrorMessageEnum.UNAUTHORIZATION);
    }
}
