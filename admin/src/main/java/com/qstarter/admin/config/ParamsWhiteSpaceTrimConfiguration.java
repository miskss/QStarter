package com.qstarter.admin.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.IOException;

/**
 * 接口请求入参统一去除空格
 *
 * @author peter
 * date 2021/1/7 10:48
 */
@Configuration
public class ParamsWhiteSpaceTrimConfiguration {

    /**
     * 去除request param 参数中字符串的空格
     */
    @ControllerAdvice
    public static class RequestParamStringTrim {
        @InitBinder
        public void bind(WebDataBinder webDataBinder) {
            StringTrimmerEditor editor = new StringTrimmerEditor(false);
            webDataBinder.registerCustomEditor(String.class, editor);
        }
    }

    @JsonComponent
    public static class RequestBodyStringTrim extends StringDeserializer {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String deserialize = super.deserialize(p, ctxt);
            return deserialize == null ? null : deserialize.trim();
        }
    }

}
