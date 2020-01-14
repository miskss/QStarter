package com.qstarter.admin.service;

import com.qstarter.admin.entity.LogMessage;
import com.qstarter.admin.model.vo.MessageLogVO;
import com.qstarter.admin.repository.LogMessageRepository;
import com.qstarter.core.utils.PageView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author peter
 * date: 2019-12-11 16:08
 **/
@Service
public class LogMessageService {
    private LogMessageRepository logMessageRepository;

    public LogMessageService( LogMessageRepository logMessageRepository) {
        this.logMessageRepository = logMessageRepository;
    }

    public void saveSysLogMsg(LogMessage logMessage) {
        logMessageRepository.save(logMessage);
    }

    public PageView<MessageLogVO> sysLogList(Integer pageIndex,Integer pageSize){
        Page<LogMessage> logMessages = logMessageRepository.findAll(PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
        return PageView.fromPage(logMessages,MessageLogVO::fromEntity);
    }

}
