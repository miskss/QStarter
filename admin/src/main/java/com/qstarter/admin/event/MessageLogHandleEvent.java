package com.qstarter.admin.event;

import com.qstarter.admin.annotations.MessageLog;
import com.qstarter.core.event.BaseEvent;

import javax.servlet.http.HttpServletRequest;

/**
 * @author peter
 * date: 2019-09-25 16:21
 **/
public class MessageLogHandleEvent extends BaseEvent<MessageLogHandleEvent.Data> {


    /**
     * Create a new ApplicationEvent.
     *
     * @param data the object on which the event initially occurred (never {@code null})
     */
    public MessageLogHandleEvent(Data data) {
        super(data);
    }

    @lombok.Data
    public static class Data {

        //入参
        private Object[] args;

        private MessageLog messageLog;

        private Object result;

        private String requestIp;

        private Long webUserId;


        public Data(Object[] args, MessageLog messageLog, Object result, String requestIp, Long webUserId) {
            this.args = args;
            this.messageLog = messageLog;
            this.result = result;
            this.requestIp = requestIp;
            this.webUserId = webUserId;
        }
    }

}
