package kiss.depot.websocket.util;

import kiss.depot.websocket.model.constant.STATIC;
import kiss.depot.websocket.model.dto.UserMessageQueueElement;
import kiss.depot.websocket.model.dto.request.WsRequest;
import kiss.depot.websocket.model.enums.CommonErr;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.model.po.GroupChatPo;
import kiss.depot.websocket.model.po.PrivateChatPo;
import kiss.depot.websocket.model.vo.response.WsResponse;
import kiss.depot.websocket.service.WebsocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* ws相关操作
* 包括session池、发送消息操作等
* author: koishikiss
* launch: 2024/12/8
* last update: 2024/12/9
* */

@Slf4j
public class WebsocketUtil {

    // websocket session池
    public static final Map<String, WebSocketSession> SESSION_MAP = new HashMap<>();

    // websocket 消息推送线程池
    public static final ExecutorService PUSH_EXECUTOR = Executors.newCachedThreadPool();

    // websocket处理层
    private static final WebsocketService websocketService = new WebsocketService();

    /**
     * 解析接收到的消息并转发给service层处理
     */
    public static void handleRequestRoute(String payload, String uid, String websocketSessionId) {
        try {
            //解析request
            WsRequest<?> request =  STATIC.objectMapper.readValue(payload, WsRequest.class);

            //根据path参数确定request处理方法
            switch (request.getPath()) {

                //私聊
                case "/chat/private" -> sendOneMessage(
                        websocketService.sendPrivateChat(
                                STATIC.objectMapper.convertValue(
                                        request.getMessage(),
                                        PrivateChatPo.class
                                ),
                                uid
                        ),
                        websocketSessionId
                );

                //群聊
                case "/chat/group" -> sendOneMessage(
                        websocketService.sendGroupChat(
                                STATIC.objectMapper.convertValue(
                                        request.getMessage(),
                                        GroupChatPo.class
                                )
                        ),
                        websocketSessionId
                );

                //找不到对应path
                default -> sendOneMessage(WsResponse.failure(CommonErr.RESOURCE_NOT_FOUND), websocketSessionId);
            }
        } catch (Exception e) {
            //告知发生错误，操作无法正确完成
            sendOneMessage(WsResponse.failure(500,e.getMessage()), websocketSessionId);
            e.printStackTrace();
        }
    }

    /**
     * 解析消息队列里得到的消息并转发给Service层处理
     */
    public static void handleMessageType(String message, String uid, String websocketSessionId) {
        try {
            if (message == null) return;

            //解析message
            UserMessageQueueElement<?> element =  STATIC.objectMapper.readValue(message, UserMessageQueueElement.class);

            //根据path参数确定message处理方法
            switch (element.getType()) {

                //收到强制登出要求
//                case "forceLogout" -> websocketService.beingForceLogout(
//                        STATIC.objectMapper.convertValue(element.getMessage(),String.class),
//                        uid,
//                        websocketSessionId
//                );

                //收到私聊消息
                case "privateChat" -> sendOneMessage(
                        websocketService.receivePrivateChat(
                                STATIC.objectMapper.convertValue(
                                        element.getMessage(),
                                        PrivateChatPo.class
                                )
                        ),
                        websocketSessionId
                );

                default -> log.info("消息队列出现未知类型：" + message);
            }
        } catch (Exception e) {
            //从左侧塞回消息
            RedisUtil.L.LIST.leftPush(RedisKey.USER_MESSAGE_LIST.concat(uid), message);
            e.printStackTrace();
        }
    }


    /**
     * 向单个用户发送一条消息
     */
    public static void sendOneMessage(WsResponse response, String websocketSessionId) {
        try {
            if (response != null)  {
                WebSocketSession webSocketSession = SESSION_MAP.get(websocketSessionId);
                if (webSocketSession != null) {
                    webSocketSession.sendMessage(new TextMessage(STATIC.objectMapper.writeValueAsString(response)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向多个用户发送同一消息
     */
    public static void sendMoreMessage(WsResponse response, String... websocketSessionIdList) {
        try {
            if (response != null) {
                TextMessage textMessage = new TextMessage(STATIC.objectMapper.writeValueAsString(response));
                for (String websocketSessionId : websocketSessionIdList) {
                    WebSocketSession webSocketSession = SESSION_MAP.get(websocketSessionId);
                    if (webSocketSession != null) {
                        webSocketSession.sendMessage(textMessage);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** 生成websocketSessionId */
    public static String generatorWebsocketSessionId(String uid, String sessionId) {
        return uid + '_' + sessionId;
    }

}
