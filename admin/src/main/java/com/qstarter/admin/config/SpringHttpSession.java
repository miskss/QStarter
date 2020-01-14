package com.qstarter.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author peter
 * date: 2019-12-05 11:15
 **/
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 6 * 3600, redisFlushMode = RedisFlushMode.IMMEDIATE)
public class SpringHttpSession {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setUseBase64Encoding(false);
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }
}

//class Initializer extends AbstractHttpSessionApplicationInitializer {
//    public Initializer() {
//        super(SpringHttpSession.class);
//    }
//}