package com.qstarter.core.event;

import com.qstarter.core.model.SmsSendResult;

/**
 * @author peter
 * date: 2019-12-11 15:34
 **/
public class SmsSendResultEvent extends BaseEvent<SmsSendResult> {
    /**
     * Create a new ApplicationEvent.
     *
     * @param data the object on which the event initially occurred (never {@code null})
     */
    public SmsSendResultEvent(SmsSendResult data) {
        super(data);
    }
}
