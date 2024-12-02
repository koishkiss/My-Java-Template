package kiss.depot.websocket.config.websocketConfig;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebsocketHandler implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        System.out.println("连接建立成功！");
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) {
        System.out.println("收到信息！");
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) {
        System.out.println("传输过程出错！");
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) {
        System.out.println("连接关闭！");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
