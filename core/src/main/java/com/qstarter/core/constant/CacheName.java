package com.qstarter.core.constant;

/**
 * 缓存的cache name 前缀
 *
 * @author peter
 * create: 2019-11-20 16:27
 **/
public interface CacheName {

    String CACHE_PREFIX = "qs:cache:";

    String WEB_USER = CACHE_PREFIX + "webuser";


    String APP_VERSION = CACHE_PREFIX + "app:version";


    /**
     * 系统相关角色资源的缓存
     */
    String SYSTEM_ROLE = CACHE_PREFIX + "sys:role";

}
