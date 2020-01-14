package com.qstarter.core.utils;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author peter
 * date: 2019-09-05 17:10
 **/
@Slf4j
public final class HttpUtils {

    private final static RestTemplate restTemplate;

    static {

        restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(1000);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(30 * 1000);
        messageConverters.add(messageConverter);

    }

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }


    /**
     * get 请求
     *
     * @param url          url
     * @param httpHeaders  请求头的设置，如 Authorization
     * @param responseType 响应类型
     * @param uriVariables 请求参数
     * @param <T>          响应类型
     * @return T
     */
    public static <T> T get(String url, HttpHeaders httpHeaders, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        try {
            if (uriVariables == null) uriVariables = new HashMap<>();
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType, uriVariables);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException codeException = (HttpStatusCodeException) e;
                log.error("返回的状态码：" + codeException.getStatusCode() + "；响应的内容：" + codeException.getResponseBodyAsString());
            }
            return null;
        }
    }


    public static <T> T get(String url, HttpHeaders httpHeaders, Class<T> responseType, MultiValueMap<String, String> requestParameters) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        if (requestParameters == null) {
            requestParameters = new LinkedMultiValueMap<>();
        }
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(requestParameters)
                .toUriString();
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, responseType);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException codeException = (HttpStatusCodeException) e;
                log.error("返回的状态码：" + codeException.getStatusCode() + "；响应的内容：" + codeException.getResponseBodyAsString());
            }
            return null;
        }
    }
    public static <T> T get(String url , ParameterizedTypeReference<T> responseType, MultiValueMap<String, String> requestParameters) {
        if (requestParameters == null) {
            requestParameters = new LinkedMultiValueMap<>();
        }
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(requestParameters)
                .toUriString();
        try {

            ResponseEntity<T> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, null, responseType);
            return responseEntity.getBody();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException codeException = (HttpStatusCodeException) e;
                log.error("返回的状态码：" + codeException.getStatusCode() + "；响应的内容：" + codeException.getResponseBodyAsString());
            }
            return null;
        }
    }
    /**
     * post 请求
     *
     * @param url          url
     * @param httpEntity   请求参数 {@link HttpEntity}
     * @param responseType 响应的类型
     * @param <T>          响应的类型
     * @return T
     * @throws RestClientException
     */
    public static <T> T post(URI url, HttpEntity<?> httpEntity, Class<T> responseType) {
        try {
            return restTemplate.postForObject(url, httpEntity, responseType);
        } catch (RestClientException e) {
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException codeException = (HttpStatusCodeException) e;

                if (Objects.equals(HttpStatus.UNAUTHORIZED, codeException.getStatusCode())) {
                    throw new SystemServiceException(ErrorMessageEnum.USERNAME_OR_PASSWORD_ERROR);
                }
                String bodyAsString = codeException.getResponseBodyAsString();
                //check body
                checkBody(bodyAsString);
            }
            log.error(e.getMessage(), e);
            throw new SystemServiceException(ErrorMessageEnum.SYSTEM_UNKNOWN_ERROR);
        }

    }

    private static void checkBody(String bodyAsString) {
        //目前只会出现账号或密码不匹配的错误
        if (bodyAsString.contains(OAuth2Exception.INVALID_GRANT)) {
            if (bodyAsString.contains("refresh token")) {
                throw new SystemServiceException(ErrorMessageEnum.INVALID_REFRESH_TOKEN);
            }
            throw new SystemServiceException(ErrorMessageEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        log.error("响应的内容：" + bodyAsString);
        throw new SystemServiceException(ErrorMessageEnum.SYSTEM_UNKNOWN_ERROR);
    }

    public static HttpHeaders createBasicHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

}
