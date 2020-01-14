package com.qstarter.app.event;

import com.qstarter.core.event.BaseEvent;

/**
 * 用户注册成功事件
 *
 * @author peter
 * date: 2019-11-26 13:43
 **/
public class AppUserRegisterEvent extends BaseEvent<Long> {
    /**
     * Create a new ApplicationEvent.
     *
     * @param appUserId the object on which the event initially occurred (never {@code null})
     */
    public AppUserRegisterEvent(Long appUserId) {
        super(appUserId);
    }
}
