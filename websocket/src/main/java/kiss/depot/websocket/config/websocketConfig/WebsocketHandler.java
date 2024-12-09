package kiss.depot.websocket.config.websocketConfig;

import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.model.vo.response.WsResponse;
import kiss.depot.websocket.util.RedisUtil;
import kiss.depot.websocket.util.WebsocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.*;

/*
* ws连接处理
* 用于连接建立、停止、收到消息时的处理
* author: koishikiss
* launch: 2024/12/2
* last update: 2024/12/8
* */

@Slf4j
public class WebsocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        String uid = session.getAttributes().get("uid").toString();

        //保存session到WebsocketSessionUtil管理
        WebsocketUtil.SESSION_MAP.put(uid,session);

        //在redis设置用户在线状态
        RedisUtil.H.set(RedisKey.USER_INFO.concat(uid), "status", "1");

        //向用户推送欢迎词
        WebsocketUtil.sendOneMessage(WsResponse.ok("/welcome!"), uid);

        //加入到推送消息推送队列

        //打印成功信息到日志
        log.info("连接建立成功！" + session.getAttributes() + "\n");
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) {
        String uid = session.getAttributes().get("uid").toString();

        //打印消息发送日志
        log.info("收到信息！"  + session.getAttributes() + "\n" + message.getPayload() + "\n");

        //转到service层解析并处理消息
        WebsocketUtil.handleRoute(message.getPayload().toString(), uid);
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) {
        String uid = session.getAttributes().get("uid").toString();

        //从消息推送队列里排除

        //将session从WebsocketSessionUtil中释放
        WebsocketUtil.SESSION_MAP.remove(uid);

        //在redis设置用户离线状态
        RedisUtil.H.set(RedisKey.USER_INFO.concat(uid), "status", "0");

        //打印长连接时的错误日志
        log.info("传输过程出错！" + session.getAttributes() + "\n");
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) {
        String uid = session.getAttributes().get("uid").toString();

        //从消息推送队列里排除

        //将session从WebsocketSessionUtil中释放
        WebsocketUtil.SESSION_MAP.remove(uid);

        //在redis设置用户离线状态
        RedisUtil.H.set(RedisKey.USER_INFO.concat(uid), "status", "0");

        //打印连接关闭信息到日志
        log.info("连接关闭！" + session.getAttributes() + "\n");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
