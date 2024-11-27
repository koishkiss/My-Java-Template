package kiss.depot.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
* redis配置类
* author: koishikiss
* launch: 2024/11/27
* last update: 2024/11/27
* */

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.password}")
    private String pwd;
    @Value("${spring.redis.database}")
    private Integer database;

    @Bean
    //Redis 连接配置
    protected RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setHostName(host); // Redis 服务器地址
        redisStandaloneConfiguration.setPort(port); // Redis 服务器端口
        redisStandaloneConfiguration.setPassword(pwd); // Redis 服务器密码
        redisStandaloneConfiguration.setDatabase(database); // Redis 数据库

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    //配置RedisTemplate序列化、反序列化规则
    protected RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        // 创建template
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 设置连接
        template.setConnectionFactory(redisConnectionFactory);

        // 设置序列化规则
        // key和hashKey采用string序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // value和hashValue采用json序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
