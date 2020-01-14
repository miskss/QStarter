package com.qstarter.admin.event;

import com.qstarter.admin.entity.LogMessage;

/**
 * @author peter
 * create: 2019-09-25 13:49
 **/
public interface LogHandler {
    LogMessage handle(MessageLogHandleEvent.Data data);
}
