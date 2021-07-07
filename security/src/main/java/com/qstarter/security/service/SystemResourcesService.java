package com.qstarter.security.service;

import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.enums.OperationEnum;
import com.qstarter.security.repository.SystemResourcesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author peter
 * date: 2019-09-09 10:45
 **/
@Service
@Transactional
public class SystemResourcesService {

    private final SystemResourcesRepository repository;

    public SystemResourcesService(SystemResourcesRepository repository) {
        this.repository = repository;
    }

    public SystemResource add(SystemResource resource) {
        SystemResource systemResource = addOrUpdate(resource);
        ResourcePool.add(systemResource);
        return systemResource;
    }

    public SystemResource update(SystemResource resource) {
        SystemResource systemResource = addOrUpdate(resource);
        ResourcePool.update(systemResource);
        return systemResource;
    }


    public void delete(Long resourceId) {
        SystemResource one = repository.findOne(resourceId);
        Long id = one.getId();
        //删除resource-role的关联关系
        repository.deleteResourceRoleRelationship(id);
        //在删除该资源
        repository.delete(one);
    }

    public boolean isResourceExist(String resourceName, OperationEnum operationEnum) {
        SystemResource nameAndOperation = repository.findByResourceNameAndOperation(resourceName, operationEnum);
        return nameAndOperation != null;
    }


    /**
     * 获取系统中 所有的资源
     *
     * @return
     */
    public Collection<SystemResource> getAllResources() {
        return ResourcePool.getALLResources();
    }

    public Page<SystemResource> findAllByPage(Pageable pageable) {
        return repository.findAll(pageable);
    }


    private SystemResource addOrUpdate(SystemResource resource) {
        return repository.save(resource);
    }
}
