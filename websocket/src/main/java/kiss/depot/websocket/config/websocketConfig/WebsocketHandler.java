package kiss.depot.websocket.config.websocketConfig;

import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.model.vo.response.WsResponse;
import kiss.depot.websocket.util.RedisUtil;
import kiss.depot.websocket.util.WebsocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.*;

import java.util.concurrent.TimeUnit;

/*
* ws连接处理
* 用于连接建立、停止、收到消息时的处理
* author: koishikiss
* launch: 2024/12/2
* last update: 2024/12/14
* */

@Slf4j
public class WebsocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        String uid = session.getAttributes().get("uid").toString();
        String websocketSessionId = session.getAttributes().get("websocketSessionId").toString();

        //保存session到WebsocketSessionUtil管理，以uid+sessionId的形式作为key
        WebsocketUtil.SESSION_MAP.put(websocketSessionId,session);

        //在redis设置用户在线状态
        RedisUtil.S.VALUE.set(RedisKey.USER_ONLINE.concat(uid), "1", 1, TimeUnit.HOURS);

        //向用户推送欢迎词
        WebsocketUtil.sendOneMessage(WsResponse.success("/welcome!", uid), websocketSessionId);

        //开启一线程用于向该用户推送消息
        WebsocketUtil.PUSH_EXECUTOR.submit(() -> {
            //当SESSION_MAP存在该用户的session时从redis循环读取阻塞队列（从左侧读取最迟消息）
            while (WebsocketUtil.SESSION_MAP.containsKey(websocketSessionId)) {
                WebsocketUtil.handleMessageType(
                        RedisUtil.L.LIST.leftPop(
                                RedisKey.USER_MESSAGE_LIST.concat(String.valueOf(uid)),
                                10,
                                TimeUnit.SECONDS
                        ),
                        uid,
                        websocketSessionId
                );
            }
        });

        //打印成功信息到日志
        log.info("连接建立成功！" + session.getAttributes() + "\n");
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) {
        String uid = session.getAttributes().get("uid").toString();
        String websocketSessionId = session.getAttributes().get("websocketSessionId").toString();

        //刷新在线状态
        RedisUtil.setExpire(RedisKey.USER_ONLINE.concat(uid), 1, TimeUnit.HOURS);

        //打印消息发送日志
        log.info("收到信息！"  + session.getAttributes() + "\n" + message.getPayload() + "\n");

        //转到service层解析并处理消息
        WebsocketUtil.handleRequestRoute(message.getPayload().toString(), uid, websocketSessionId);
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) {
        String uid = session.getAttributes().get("uid").toString();
        String websocketSessionId = session.getAttributes().get("websocketSessionId").toString();

        //结束消息推送线程
        //将session从WebsocketSessionUtil中释放
        WebsocketUtil.SESSION_MAP.remove(websocketSessionId);

        //在redis设置用户离线状态
        RedisUtil.delete(RedisKey.USER_ONLINE.concat(uid));

        //打印长连接时的错误日志
        log.info("传输过程出错！" + session.getAttributes() + "\n");
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) {
        String uid = session.getAttributes().get("uid").toString();
        String websocketSessionId = session.getAttributes().get("websocketSessionId").toString();

        //从消息推送队列里排除
        //将session从WebsocketSessionUtil中释放
        WebsocketUtil.SESSION_MAP.remove(websocketSessionId);

        //在redis设置用户离线状态
        RedisUtil.delete(RedisKey.USER_ONLINE.concat(uid));

        //打印连接关闭信息到日志
        log.info("连接关闭！" + session.getAttributes() + "\n");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
