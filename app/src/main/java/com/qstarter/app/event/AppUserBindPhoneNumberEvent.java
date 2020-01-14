package com.qstarter.app.event;

import com.qstarter.core.event.BaseEvent;

/**
 * @author peter
 * date: 2019-12-18 15:27
 **/
public class AppUserBindPhoneNumberEvent extends BaseEvent<Long> {
    /**
     * Create a new ApplicationEvent.
     *
     * @param data the object on which the event initially occurred (never {@code null})
     */
    public AppUserBindPhoneNumberEvent(Long data) {
        super(data);
    }
}
