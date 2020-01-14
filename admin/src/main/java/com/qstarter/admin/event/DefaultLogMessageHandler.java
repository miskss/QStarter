package com.qstarter.admin.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qstarter.admin.annotations.MessageLog;
import com.qstarter.admin.entity.LogMessage;
import com.qstarter.admin.enums.LogActionTypeEnum;
import com.qstarter.core.model.GenericMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author peter
 * date: 2019-12-19 13:53
 **/
@Component("DefaultLogMessageHandler")
@Slf4j
public class DefaultLogMessageHandler implements LogHandler {

    private final static ObjectMapper om = new ObjectMapper();

    @Override
    public LogMessage handle(MessageLogHandleEvent.Data data) {
        MessageLog messageLog = data.getMessageLog();
        Object[] args = data.getArgs();

        Object result = data.getResult();

        String title = messageLog.value();

        LogActionTypeEnum action = messageLog.action();

        StringBuilder content = new StringBuilder();

        try {
            String value = om.writeValueAsString(args);
            content.append("入参为").append(value);

            if (action == LogActionTypeEnum.UPDATE) {
                //只有修改操作才需要记录修改后的返回结果
                if (result instanceof GenericMsg) {
                    result = ((GenericMsg) result).getData();
                }
                String value1 = om.writeValueAsString(result);
                content.append("返回的结果为【").append(value1).append("】");
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return new LogMessage("", title, action, content.toString());
    }
}
