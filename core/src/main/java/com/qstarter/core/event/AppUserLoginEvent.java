package com.qstarter.core.event;

/**
 * app 用户登录事件
 *
 * @author peter
 * date: 2019-11-20 15:55
 **/
public class AppUserLoginEvent extends BaseEvent<AppUserLoginEvent.Data> {


    /**
     * Create a new ApplicationEvent.
     *
     * @param data the object on which the event initially occurred (never {@code null})
     */
    public AppUserLoginEvent(Data data) {
        super(data);
    }

    @lombok.Data
    public static class Data {

        private Long appUserId;

        private boolean isNewUser;

        public Data(Long appUserId, boolean isNewUser) {
            this.appUserId = appUserId;
            this.isNewUser = isNewUser;
        }
    }

}
