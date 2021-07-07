package com.qstarter.security.service;

import com.qstarter.security.entity.SystemResource;
import com.qstarter.security.repository.SystemResourcesRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 后台网页的资源池
 * 项目启动时加载系统的所有资源到系统中
 *
 * @author peter
 * date: 2019-09-06 15:05
 **/
@Component
public class ResourcePool {

    private static final ConcurrentHashMap<Long, SystemResource> RESOURCES = new ConcurrentHashMap<>(32);

    private final SystemResourcesRepository repository;

    public ResourcePool(SystemResourcesRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initPool() {
        List<SystemResource> all = repository.findAll();
        all.forEach(r -> RESOURCES.put(r.getId(), r));
    }

    public static Set<Long> getAllResourcesId() {
        HashSet<Long> idSet = new HashSet<>();
        Enumeration<Long> keys = RESOURCES.keys();
        while (keys.hasMoreElements()) {
            Long id = keys.nextElement();
            idSet.add(id);
        }
        return idSet;
    }

    public static Collection<SystemResource> getALLResources() {
        return RESOURCES.values();
    }

    public static void add(SystemResource resources) {
        RESOURCES.put(resources.getId(), resources);
    }

    public static void delete(Long resourceId) {
        RESOURCES.remove(resourceId);
    }

    public static SystemResource get(Long resourcesId) {
        return RESOURCES.get(resourcesId);
    }

    public static void update(SystemResource resources) {
        RESOURCES.replace(resources.getId(), resources);
    }
}
