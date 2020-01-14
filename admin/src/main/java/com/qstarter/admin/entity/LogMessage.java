package com.qstarter.admin.entity;

import com.qstarter.admin.enums.LogActionTypeEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author peter
 * date: 2019-09-24 09:44
 **/
@Data
@Entity
public class LogMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    private String title;

    @Enumerated(EnumType.STRING)
    private LogActionTypeEnum actionType;

    private String content;

    private String webUser;

    public LogMessage() {
    }

    public LogMessage(String webUser, String title, LogActionTypeEnum actionType, String content) {
        this.webUser = webUser;
        this.title = title;
        this.actionType = actionType;
        this.content = content;
        this.createTime = LocalDateTime.now();
    }
}
