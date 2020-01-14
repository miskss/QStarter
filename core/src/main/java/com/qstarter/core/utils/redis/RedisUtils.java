package com.qstarter.core.utils.redis;

import com.google.common.base.Strings;
import com.qstarter.core.constant.RedisKey;
import lombok.Data;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-05-06 09:40
 **/
@Component
@Data
public class RedisUtils {

    private RedisTemplate<String, Object> redisTemplate;
    private static RedisTemplate<String, Object> innerTemplate;

    public static ValueOperations<String, Object> valueOperations;
    public static HashOperations<String, Object, Object> hashOperations;
    private static ListOperations<String, Object> listOperations;
    private static SetOperations<String, Object> setOperations;
    private static ZSetOperations<String, Object> zSetOperations;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        innerTemplate = redisTemplate;
        //string
        valueOperations = redisTemplate.opsForValue();
        //hash
        hashOperations = redisTemplate.opsForHash();
        //list
        listOperations = redisTemplate.opsForList();
        //set
        setOperations = redisTemplate.opsForSet();
        //ZSet
        zSetOperations = redisTemplate.opsForZSet();

    }


    public static <T> void setStringValueExpire(String key, T value) {
        if (key != null && value != null) {
            valueOperations.set(getKey(key), value);
        }
    }

    public static Object getStringValue(String key) {
        if (key == null) return null;
        return valueOperations.get(getKey(key));
    }

    public static <T> void setStringValueExpire(String key, T value, long expire) {
        if (key != null && value != null) {
            valueOperations.set(getKey(key), value, expire, TimeUnit.SECONDS);
        }
    }


    public static Long incrString(String key) {
        return valueOperations.increment(getKey(key));
    }

    public static Long getKeyExpireTime(String key, TimeUnit timeUnit) {
        return innerTemplate.getExpire(getKey(key), timeUnit);
    }

    public static boolean hasKey(String key) {
        Boolean aBoolean = innerTemplate.hasKey(getKey(key));
        return aBoolean != null ? aBoolean : false;
    }

    public static boolean delete(String key) {
        Boolean delete = innerTemplate.delete(getKey(key));
        return delete != null ? delete : false;
    }

    public static Long delete(List<String> keys) {
        return innerTemplate.delete(getKey(keys));
    }

    public static Long setListValue(String key, Object... values) {
        validKey(key);
        return listOperations.leftPushAll(getKey(key), values);
    }

    public static Long setListValue(String key, Collection<Object> objects) {
        validKey(key);
        if (objects == null || objects.isEmpty())
            throw new IllegalArgumentException("objects must not be null or empty");
        return listOperations.leftPushAll(getKey(key), objects);
    }

    public static Object rightPop(String key) {
        validKey(key);
        return listOperations.rightPop(getKey(key));
    }

    public static List<Object> getList(String key) {
        validKey(key);
        if (!hasKey(key)) {
            return new ArrayList<>();
        }
        Long size = listOperations.size(getKey(key));
        return listOperations.range(getKey(key), 0, size);
    }

    public static long setSet(String key, Object... value) {
        validKey(key);
        Long add = setOperations.add(getKey(key), value);

        if (add == null) throw new IllegalArgumentException("redis set add failure");

        return add;
    }


    /**
     * 清空key下的值重新赋值
     *
     * @param key
     * @param values
     */
    public static void cleanAndSet(String key, Set<Object> values) {
        validKey(key);
        //删除原的key
        delete(key);
        if (!values.isEmpty()) {
            //重新设置值
            setOperations.add(getKey(key), values.toArray());
        }
    }

    public static long removeSetElement(String key, Object value) {

        validKey(key);

        Long remove = setOperations.remove(getKey(key), value);
        if (remove == null) throw new IllegalArgumentException("redis set remove failure");
        return remove;
    }


    public static long getSetSize(String key) {
        validKey(key);
        Long size = setOperations.size(getKey(key));
        return size == null ? 0 : size;
    }

    public static boolean hasHashKey(String key, String hashKey) {
        validKey(key);
        return hashOperations.hasKey(getKey(key), hashKey);
    }

    public static <V> void putAllHash(String key, Map<String, V> map) {
        validKey(key);
        Assert.notNull(map, "map must not be null!");
        Assert.notEmpty(map, "map must not empty!");
        hashOperations.putAll(getKey(key), map);
    }


    public static <K, V> Boolean putIfAbsent(String key, K hKey, V hVal) {
        validKey(key);
        Assert.notNull(hKey, "hKey must not be null");
        Assert.notNull(hKey, "hVal must not be null");
        return hashOperations.putIfAbsent(getKey(key), hKey, hVal);

    }

    public static void putHash(String key, String hashKey, Object hashValue) {
        validKey(key);
        Assert.notNull(hashKey, "hashKey must not be null!");
        Assert.notNull(hashValue, "hashValue must not be null!");
        hashOperations.put(getKey(key), hashKey, hashValue);
    }

    public static Long increment(String key, String hashKey, Integer increment) {
        validKey(key);
        Assert.notNull(hashKey, "hashKey must not be null!");
        Assert.notNull(increment, "hashKey must not be null!");
        return hashOperations.increment(getKey(key), hashKey, increment);
    }


    public static Object getHashKeyValue(String key, String hashKey) {
        validKey(key);
        return hashOperations.get(getKey(key), hashKey);
    }

    public static Long deleteHashKeyValue(String key, Object hashKey) {
        validKey(key);
        return hashOperations.delete(getKey(key), hashKey);
    }

    public static Map<Object, Object> getHash(String key) {
        validKey(key);
        return hashOperations.entries(getKey(key));
    }


    public static void setHashKeyValue(String key, Object hashKey, Object hashValue) {
        hashOperations.put(getKey(key), hashKey, hashValue);
    }


    public static Set<Object> getSet(String key) {
        validKey(key);
        Set<Object> members = setOperations.members(getKey(key));
        return members == null ? new HashSet<>() : members;
    }

    private static String getKey(String key) {
        return RedisKey.PREFIX_KEY + key;
    }

    private static List<String> getKey(List<String> keys) {

        return keys.stream().map(RedisUtils::getKey).collect(Collectors.toList());
    }

    private static void validKey(String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new IllegalArgumentException("key must not be empty or null");
        }
    }


}
