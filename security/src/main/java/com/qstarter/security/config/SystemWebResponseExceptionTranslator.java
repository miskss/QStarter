package com.qstarter.security.config;

import com.qstarter.core.model.GenericErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;

/**
 * @author peter
 * date: 2019-09-05 08:36
 **/
@Slf4j
public class SystemWebResponseExceptionTranslator implements WebResponseExceptionTranslator<GenericErrorMessage> {
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<GenericErrorMessage> translate(Exception e) throws Exception {

        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);

        if (ase != null) {
            return handleOAuth2Exception((OAuth2Exception) ase);
        }

        ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new SystemWebResponseExceptionTranslator.UnauthorizedException(e.getMessage(), e));
        }

        ase = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new SystemWebResponseExceptionTranslator.ForbiddenException(ase.getMessage(), ase));
        }

        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(
                HttpRequestMethodNotSupportedException.class, causeChain);

        if (ase != null) {
            return handleOAuth2Exception(new SystemWebResponseExceptionTranslator.MethodNotAllowed(ase.getMessage(), ase));
        }

        log.error(e.getMessage(), e);

        return handleOAuth2Exception(new SystemWebResponseExceptionTranslator.ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
    }

    private ResponseEntity<GenericErrorMessage> handleOAuth2Exception(OAuth2Exception e) throws IOException {

        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }
        GenericErrorMessage message = new GenericErrorMessage(status, e.getOAuth2ErrorCode() + " [ " + e.getMessage() + " ]");

        return new ResponseEntity<>(message, headers,
                HttpStatus.valueOf(status));

    }

    public static class ForbiddenException extends OAuth2Exception {

        public ForbiddenException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "权限不足，禁止访问！";
        }

        @Override
        public int getHttpErrorCode() {
            return 403;
        }

    }

    @SuppressWarnings("serial")
    private static class ServerErrorException extends OAuth2Exception {

        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "服务器异常";
        }

        @Override
        public int getHttpErrorCode() {
            return 500;
        }

    }

    @SuppressWarnings("serial")
    private static class UnauthorizedException extends OAuth2Exception {

        UnauthorizedException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "无效的令牌";
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }

    }

    @SuppressWarnings("serial")
    private static class MethodNotAllowed extends OAuth2Exception {

        MethodNotAllowed(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "method_not_allowed";
        }

        @Override
        public int getHttpErrorCode() {
            return 405;
        }

    }


}
