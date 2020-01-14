package com.qstarter.core.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 事件的基类
 *
 * @author peter
 * date: 2019-09-23 17:14
 **/
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseEvent<T> extends ApplicationEvent {

    private T data;

    /**
     * Create a new ApplicationEvent.
     *
     * @param data the object on which the event initially occurred (never {@code null})
     */
    public BaseEvent(T data) {
        super(data);
        this.data = data;
    }
}
