package com.qstarter.admin.controller;

import com.qstarter.admin.model.vo.MessageLogVO;
import com.qstarter.admin.service.LogMessageService;
import com.qstarter.core.model.GenericMsg;
import com.qstarter.core.utils.PageView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author peter
 * date: 2019-12-11 16:05
 **/
@RestController
@RequestMapping("/api/web/log")
public class LogMessageController {
    private LogMessageService logMessageService;

    public LogMessageController(LogMessageService logMessageService) {
        this.logMessageService = logMessageService;
    }

    @GetMapping("/sys-log")
    @PreAuthorize("hasPermission('','')")
    public GenericMsg<PageView<MessageLogVO>> sysLog(@RequestParam(defaultValue = "0") Integer pageIndex,
                                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        return GenericMsg.success(logMessageService.sysLogList(pageIndex, pageSize));
    }
}
