package kiss.depot.websocket.model.constant;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class STATIC {

    private STATIC() {}

    public static final ObjectMapper objectMapper = new ObjectMapper();

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
