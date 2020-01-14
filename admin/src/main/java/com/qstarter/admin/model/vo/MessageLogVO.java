package com.qstarter.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qstarter.admin.entity.LogMessage;
import com.qstarter.core.utils.TimeUtils;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author peter
 * date: 2019-12-19 14:46
 **/
@Data
public class MessageLogVO {
    @JsonFormat(pattern = TimeUtils.TIME_PATTERN)
    private LocalDateTime createTime;

    private String title;

    private String actionType;

    private String content;

    private String webUser;


    public static MessageLogVO fromEntity(LogMessage log){
        MessageLogVO messageLogVO = new MessageLogVO();
        messageLogVO.setCreateTime(log.getCreateTime());
        messageLogVO.setTitle(log.getTitle());
        messageLogVO.setActionType(log.getActionType().getDescr());
        messageLogVO.setContent(log.getContent());
        messageLogVO.setWebUser(log.getWebUser());
        return messageLogVO;}

}
