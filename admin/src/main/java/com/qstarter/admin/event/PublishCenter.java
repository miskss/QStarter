package com.qstarter.admin.event;

import com.qstarter.admin.annotations.MessageLog;
import com.qstarter.core.event.PublishEventCenter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author peter
 * date: 2019-09-25 17:37
 **/
public class PublishCenter {


    public static void publishMessageLogHandleEvent(Object[] args, MessageLog messageLog, Object result, HttpServletRequest request,Long webUserId){

        MessageLogHandleEvent.Data data = new MessageLogHandleEvent.Data(args, messageLog, result, request,webUserId);

        PublishEventCenter.getPublisher().publishEvent(new MessageLogHandleEvent(data));

    }



}
