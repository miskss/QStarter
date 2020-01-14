package com.qstarter.app.event;

import com.qstarter.core.event.BaseEvent;

/**
 * 用户绑定微信事件
 * @author peter
 * date: 2019-12-18 15:28
 **/
public class AppUserBindWXEvent extends BaseEvent<Long> {
    /**
     * Create a new ApplicationEvent.
     *
     * @param data the object on which the event initially occurred (never {@code null})
     */
    public AppUserBindWXEvent(Long data) {
        super(data);
    }
}
