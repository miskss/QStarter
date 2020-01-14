package com.qstarter.core.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author peter
 * date: 2019-12-11 15:34
 **/
@Data
public class SmsSendResult {

    private LocalDateTime sendTime;

    private String phones;

    private String content;

    private boolean send;

    private String sendMsg;

    public SmsSendResult(String phones, String content, boolean send, String sendMsg) {
        this.sendTime = LocalDateTime.now();
        this.phones = phones;
        this.content = content;
        this.send = send;
        this.sendMsg = sendMsg;
    }
}
