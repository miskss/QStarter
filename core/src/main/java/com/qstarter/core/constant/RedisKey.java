package com.qstarter.core.constant;

/**
 * @author peter
 * create: 2019-06-05 09:08
 **/
public interface RedisKey {

    /**
     * 全局数据存储的key前缀
     */
    String PREFIX_KEY = "qs:data:";

    String SMS_CODE_KEY = "sms:";

    //短时间内发送的短信的ip
    String CODE_IP = "codeIp:";

    //封禁的ip
    String BAN_IP = "banIp:";





}
