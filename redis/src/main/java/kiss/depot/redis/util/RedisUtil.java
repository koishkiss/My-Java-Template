package kiss.depot.redis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static kiss.depot.redis.model.constant.STATIC.objectMapper;

/*
* redis工具类
* author: koishikiss
* launch: 2024/11/27
* last update: 2024/12/15
* */

@Component
public class RedisUtil {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    public static RedisTemplate<String, String> redis;

    @PostConstruct
    @SuppressWarnings("unused")
    public void init() {
        // 静态注入
        redis = redisTemplate;
    }

    /** 设置键的过期时间，单位秒 */
    public static boolean setExpire(String key, long expire) {
        return Boolean.TRUE.equals(redis.expire(key, expire, TimeUnit.SECONDS));
    }

    /** 设置键的过期时间，并指定单位 */
    public static boolean setExpire(String key, long expire, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redis.expire(key, expire, timeUnit));
    }

    /** 删除单个键 */
    public static boolean delete(String key) {
        return Boolean.TRUE.equals(redis.delete(key));
    }

    /** 删除多个键 */
    public static Long delete(Collection<String> keys) {
        return redis.delete(keys);
    }

    /**
     * String类型数据操作
     */
    public static class S {

        // 设置静态对象来完成对String类型数据操作
        public static final ValueOperations<String, String> VALUE = redis.opsForValue();

        /** 添加一个键值对，值为对象 */
        public static <T> void setObject(String key, T value) {
            try {
                // 使用ObjectMapper将对象转化为json
                VALUE.set(key, objectMapper.writeValueAsString(value));
            } catch (JsonProcessingException e) {
                //错误处理
                throw new RuntimeException(e);
            }
        }

        /** 获取一个对象类型值 */
        public static <T> T getObject(String key, Class<T> clazz) {
            try {
                // 通过ObjectMapper将json转换为对象
                return objectMapper.readValue(VALUE.get(key), clazz);
            } catch (JsonProcessingException e) {
                //错误处理
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Hash类型数据操作
     */
    public static class H {

        /** 设置静态对象来完成对Hash类型数据操作 */
        public static final HashOperations<String,String,String> HASH = redis.opsForHash();

        /** 同时将整个对象存入Hash键 */
        public static void setObject(String key, Object object) {
            HASH.putAll(key, objectMapper.convertValue(object, new TypeReference<Map<String, String>>() {}));
        }

        /** 同时将整个对象从HASH中取出 */
        public static <T> T getObject(String key, Class<T> clazz) {
            List<String> fields = ObjectUtil.getFields(clazz);
            List<String> values = HASH.multiGet(key, fields);
            Map<String, String> m = new HashMap<>();
            for (int i = 0; i < fields.size(); i++) {
                m.put(fields.get(i), values.get(i));
            }
            return objectMapper.convertValue(m, clazz);
        }

    }

    /**
     * List类型操作
     */
    public static class L {

        /** 设置静态对象来完成对List类型数据操作 */
        public static final ListOperations<String, String> LIST = redis.opsForList();

        /** 在左侧插入对象的json串 */
        public static <T> void leftPushObject(String key, T element) {
            try {
                LIST.leftPush(key,objectMapper.writeValueAsString(element));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        /** 在右侧插入对象的json串 */
        public static <T> void rightPushObject(String key, T element) {
            try {
                LIST.rightPush(key,objectMapper.writeValueAsString(element));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        /** 在左侧弹出对象的json串 */
        public static <T> T leftPopObject(String key, Class<T> clazz) {
            try {
                return objectMapper.readValue(LIST.leftPop(key),clazz);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        /** 在右侧弹出对象的json串 */
        public static <T> T rightPopObject(String key, Class<T> clazz) {
            try {
                return objectMapper.readValue(LIST.rightPop(key),clazz);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
