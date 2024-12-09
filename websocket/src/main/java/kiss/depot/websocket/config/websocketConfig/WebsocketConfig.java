package kiss.depot.websocket.config.websocketConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/*
* ws配置类
* author: koishikiss
* launch: 2024/12/2
* last update: 2024/12/8
* */

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebsocketHandler(), "/connect")  //连接时信息处理配置
                .addInterceptors(new WebsocketInterceptor())  //过滤器配置
                .setAllowedOrigins("*");  //跨域配置
    }

}
