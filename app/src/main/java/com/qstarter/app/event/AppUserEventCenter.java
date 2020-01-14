package com.qstarter.app.event;

import com.qstarter.core.event.AppUserLoginEvent;
import com.qstarter.core.event.PublishEventCenter;

/**
 * @author peter
 * date: 2019-11-21 16:07
 **/
public class AppUserEventCenter {

    /**
     * 用户注册登录事件
     *
     * @param appUserId 用户id
     * @param isNewUser 是不是新用户
     */
    public static void publishAppUserLoginEvent(Long appUserId, boolean isNewUser) {
        PublishEventCenter.getPublisher().publishEvent(new AppUserLoginEvent(new AppUserLoginEvent.Data(appUserId, isNewUser)));
    }



    /**
     * 用户注册事件
     *
     * @param appUserId 注册后id
     */
    public static void publishAppUserRegisterEvent(Long appUserId) {
        PublishEventCenter.getPublisher()
                .publishEvent(new AppUserRegisterEvent(appUserId));
    }

    /**
     * 用户绑定手机号事件
     *
     * @param appUserId 用户id
     */
    public static void publishAppUserBindPhoneNumberEvent(Long appUserId) {
        PublishEventCenter.getPublisher()
                .publishEvent(new AppUserBindPhoneNumberEvent(appUserId));
    }

    /**
     * 用户绑定微信事件
     *
     * @param appUserId 用户id
     */
    public static void publishAppUserBindWXEvent(Long appUserId) {
        PublishEventCenter.getPublisher()
                .publishEvent(new AppUserBindWXEvent(appUserId));
    }


}
