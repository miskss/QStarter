package com.qstarter.app.repository;

import com.qstarter.app.entity.AppVersion;
import com.qstarter.core.enums.DeviceTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author peter
 * create: 2019-12-20 08:58
 **/
public interface AppVersionRepository extends JpaRepository<AppVersion,Long> {
    List<AppVersion> findByDeviceEqualsOrderByCreateTimeDesc(DeviceTypeEnum deviceTypeEnum);
}
