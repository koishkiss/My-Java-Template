package kiss.depot.redis.util;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/*
* redis工具类
* author: koishikiss
* launch: 2024/11/27
* last update: 2024/11/27
* */

@Component
public class RedisUtil {

    @PostConstruct
    public void init() {
        // 静态注入
        redis = redisTemplate;
    }

    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    static RedisTemplate<Object, Object> redis;

    public static void set(String key, Object value) {
        redis.opsForValue().set(key,value);
    }


}
