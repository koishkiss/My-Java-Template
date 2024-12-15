package kiss.depot.websocket.model.constant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class STATIC {

    private STATIC() {}

    public static final ObjectMapper objectMapper;

    //一些静态变量需要静态初始化
    static {
        //objectMapper静态初始化
        objectMapper = new ObjectMapper();

        //定义序列化、反序列化模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule);

    }

    //用于提供yml文件中的各种value（仅多处使用时添加）
    @Component
    public static class VALUE {
        @PostConstruct
        @SuppressWarnings("unused")
        public void init() {
            jwt_expire = JWT_EXPIRE;
            jwt_secret = JWT_SECRET;
            user_info_expire = USER_INFO_EXPIRE;
        }

        //jwt过期时间
        @Value("${jwt.expire:604800000}")
        long JWT_EXPIRE;
        public static long jwt_expire;

        //jwt密钥
        @Value("${jwt.secret:the-default-secret}")
        String JWT_SECRET;
        public static String jwt_secret;

        @Value("${cache.user.info.expire:604800000}")
        long USER_INFO_EXPIRE;
        public static long user_info_expire;

    }
}
