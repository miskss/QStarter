package com.qstarter.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author peter
 * date: 2019-10-16 09:23
 **/
@Configuration
public class RedisConfig extends CachingConfigurerSupport {


    public static final String ONE_HOUR_EXPIRE_CACHE_MANAGER_NAME = "oneHourExpireCache";

    //注入一个可序列object的template
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> serializer = getObjectJackson2JsonRedisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    @Primary
    public RedisCacheManager cacheManagerDefault(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = getRedisCacheConfiguration();
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();
    }

    @Bean(ONE_HOUR_EXPIRE_CACHE_MANAGER_NAME)
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = getRedisCacheConfiguration();
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(configuration.entryTtl(Duration.ofHours(1)))
                .build();
    }

    private Jackson2JsonRedisSerializer<Object> getObjectJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new Hibernate5Module()); // new module, NOT JSR310Module;
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        objectMapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    private RedisCacheConfiguration getRedisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getObjectJackson2JsonRedisSerializer()));
    }
}
