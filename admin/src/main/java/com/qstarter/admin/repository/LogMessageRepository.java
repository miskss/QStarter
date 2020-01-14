package com.qstarter.admin.repository;

import com.qstarter.admin.entity.LogMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author peter
 * create: 2019-12-19 11:35
 **/
public interface LogMessageRepository extends JpaRepository<LogMessage,Long> {
}
