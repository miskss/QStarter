package com.qstarter.admin.event;

import com.qstarter.admin.annotations.MessageLog;
import com.qstarter.admin.entity.LogMessage;
import com.qstarter.admin.enums.LogActionTypeEnum;
import com.qstarter.admin.model.dto.LoginDTO;
import com.qstarter.core.utils.RemoteIpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author peter
 * date: 2019-09-25 14:09
 **/
@Component(value = "LoginLogHandler")
@Slf4j
public class LoginLogHandler implements LogHandler {
    @Override
    public LogMessage handle(MessageLogHandleEvent.Data data) {
        Object[] args = data.getArgs();

        LoginDTO dto = (LoginDTO) args[0];
        String username = dto.getUsername();
        String localAddr = RemoteIpHelper.getClientIpAddress(data.getRequest());
        MessageLog messageLog = data.getMessageLog();
        LogActionTypeEnum action = messageLog.action();
        String title = messageLog.value();
        return new LogMessage(username, title, action, "登录ip：" + localAddr);
    }

}
