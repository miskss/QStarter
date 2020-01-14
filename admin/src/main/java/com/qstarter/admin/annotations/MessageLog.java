package com.qstarter.admin.annotations;

import com.qstarter.admin.enums.LogActionTypeEnum;
import com.qstarter.admin.event.DefaultLogMessageHandler;
import com.qstarter.admin.event.LogHandler;

import java.lang.annotation.*;

/**
 * @author peter
 * create: 2019-09-25 09:15
 **/
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
public @interface MessageLog {

    /**
     * 执行的动作，增删改查
     *
     * @return {@link LogActionTypeEnum}
     */
    LogActionTypeEnum action() default LogActionTypeEnum.QUERY;

    /**
     * 日志描述
     *
     * @return string
     */
    String value();

    /**
     * 日志的处理
     *
     * @return {@link LogHandler}
     */
    Class<? extends LogHandler> handler() default DefaultLogMessageHandler.class;
}
