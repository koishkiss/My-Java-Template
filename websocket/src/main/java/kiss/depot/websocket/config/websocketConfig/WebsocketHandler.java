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
        //保存session到WebsocketSessionUtil管理

        //在redis设置用户在线状态

        //向用户推送欢迎词

        //建立新线程用于推送消息

        //打印成功信息到日志
        log.info("连接建立成功！" + session.getAttributes() + "\n");
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) {
        //打印消息发送日志
        log.info("收到信息！"  + session.getAttributes() + "\n" + message.getPayload() + "\n");
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) {
        //打印长连接时的错误日志
        log.info("传输过程出错！" + session.getAttributes() + "\n");
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) {
        //将session从WebsocketSessionUtil中释放

        //在redis设置用户离线状态

        //关闭用于推送消息的线程

        //打印连接关闭信息到日志
        log.info("连接关闭！" + session.getAttributes() + "\n");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
