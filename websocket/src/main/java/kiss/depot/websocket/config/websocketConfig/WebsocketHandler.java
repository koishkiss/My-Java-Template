package kiss.depot.websocket.config.websocketConfig;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class WebsocketHandler implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        // 从redis获取连接id

        log.info("连接建立成功！" + session.getAttributes());
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) {
        log.info("收到信息！" + message.getPayload());
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) {
        log.info("传输过程出错！");
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) {
        log.info("连接关闭！");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
