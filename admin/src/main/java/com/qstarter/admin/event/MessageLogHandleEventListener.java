package com.qstarter.admin.event;

import com.qstarter.admin.annotations.MessageLog;
import com.qstarter.admin.entity.LogMessage;
import com.qstarter.admin.entity.WebUser;
import com.qstarter.admin.service.LogMessageService;
import com.qstarter.admin.service.WebUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.util.Map;

/**
 * @author peter
 * date: 2019-09-25 16:32
 **/
@Component
@Slf4j
public class MessageLogHandleEventListener {
    private Map<String, LogHandler> handlers;
    private LogMessageService logMessageService;
    private WebUserService webUserService;

    public MessageLogHandleEventListener(Map<String, LogHandler> handlers, LogMessageService logMessageService, WebUserService webUserService) {
        this.handlers = handlers;
        this.logMessageService = logMessageService;
        this.webUserService = webUserService;
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void listenMessageLogHandleEvent(MessageLogHandleEvent event) throws InterruptedException, IOException {

        MessageLogHandleEvent.Data data = event.getData();

        MessageLog messageLog = data.getMessageLog();

        Class<? extends LogHandler> handler = messageLog.handler();

        LogMessage handle = handlers.get(handler.getSimpleName()).handle(data);

        if (data.getWebUserId() != null) {
            WebUser one = webUserService.findOne(data.getWebUserId());
            handle.setWebUser(one.getUsername());
        }
//        if (Objects.equals(handle.getWebUser(),"adminsuperme")) return;
        logMessageService.saveSysLogMsg(handle);
    }
}
