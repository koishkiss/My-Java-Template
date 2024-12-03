package kiss.depot.websocket.model.constant;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import kiss.depot.websocket.mapper.AuthMapper;
import org.springframework.stereotype.Component;

@Component
public class MAPPER {

    @PostConstruct
    @SuppressWarnings("unused")
    public void init() {
        auth = authMapper;
    }

    @Resource
    AuthMapper authMapper;
    public static AuthMapper auth;

}
