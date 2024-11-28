package kiss.depot.redis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static kiss.depot.redis.model.constant.STATIC.objectMapper;

/*
* redis工具类
* author: koishikiss
* launch: 2024/11/27
* last update: 2024/11/27
* */

@Component
public class RedisUtil {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    private static RedisTemplate<String, String> redis;

    @PostConstruct
    @SuppressWarnings("unused")
    public void init() {
        // 静态注入
        redis = redisTemplate;
//        S.VALUE = redisTemplate.opsForValue();
//        H.HASH = redisTemplate.opsForHash();
    }

    // 获取键的过期时间，单位秒
    public static long getExpire(String key) {
        Long expire = redis.getExpire(key);
        return expire == null ? -2 : expire;
    }

    // 获取键的过期时间，并指定单位
    public static long getExpire(String key, TimeUnit timeUnit) {
        Long expire = redis.getExpire(key, timeUnit);
        return expire == null ? -2 : expire;
    }

    // 设置键的过期时间，单位秒
    public static boolean setExpire(String key, long expire) {
        return Boolean.TRUE.equals(redis.expire(key, expire, TimeUnit.SECONDS));
    }

    // 设置键的过期时间，并指定单位
    public static boolean setExpire(String key, long expire, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redis.expire(key, expire, timeUnit));
    }

    // 删除单个键
    public static boolean delete(String key) {
        return Boolean.TRUE.equals(redis.delete(key));
    }

    // 删除多个键
    public static long delete(Collection<String> keys) {
        Long deletes = redis.delete(keys);
        return deletes == null ? -1 : deletes;
    }

    /**
     * String类型数据操作
     */
    public static class S {

        // 设置静态对象来完成对String类型数据操作
        private static final ValueOperations<String, String> VALUE = redis.opsForValue();

        // 添加一个键值对，值为String
        public static void set(String key, String value) {
            VALUE.set(key,value);
        }

        // 添加一个键值对，值为对象
        public static <T> void setObject(String key, T value) {
            try {
                // 使用ObjectMapper将对象转化为json
                VALUE.set(key, objectMapper.writeValueAsString(value));
            } catch (JsonProcessingException e) {
                //错误处理
                throw new RuntimeException(e);
            }
        }

        // 获取一个String类型值
        public static String get(String key) {
            return VALUE.get(key);
        }

        // 获取一个对象类型值
        public static <T> T getObject(String key, Class<T> clazz) {
            try {
                // 通过ObjectMapper将json转换为对象
                return objectMapper.readValue(VALUE.get(key), clazz);
            } catch (JsonProcessingException e) {
                //错误处理
                throw new RuntimeException(e);
            }
        }

        //可以添加更多操作

    }

    /**
     * Hash类型数据操作
     */
    public static class H {

        // 设置静态对象来完成对Hash类型数据操作
        private static final HashOperations<String,String,String> HASH = redis.opsForHash();

        // 键-字段-值 形式的Hash设置操作
        public static void set(String key, String field, String value) {
            HASH.put(key, field, value);
        }

        // 根据 键-字段 来获取值
        public static String get(String key, String field) {
            return HASH.get(key, field);
        }

    }


}
