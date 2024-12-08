package kiss.depot.websocket.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import kiss.depot.websocket.model.constant.STATIC;
import kiss.depot.websocket.model.vo.response.WsResponse;
import kiss.depot.websocket.service.WebsocketService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
* ws相关操作
* 包括session池、发送消息操作等
* author: koishikiss
* launch: 2024/12/8
* last update: 2024/12/8
* */

public class WebsocketUtil {

    // websocket session池
    public static final Map<String, WebSocketSession> SESSION_MAP = new HashMap<>();

    // websocket处理层
    private static final WebsocketService websocketService = new WebsocketService();

    /**
     * 解析接收到的消息并转发给service层处理
     */
    public static void handleRoute(String payload, String uid) {
        try {
            sendOneMessage(websocketService.test(STATIC.objectMapper.readValue(payload, new TypeReference<>() {})), uid);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 向单个用户发送一条消息
     */
    public static void sendOneMessage(WsResponse response, String uid) {
        try {
            SESSION_MAP.get(uid).sendMessage(new TextMessage(STATIC.objectMapper.writeValueAsString(response)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向多个用户发送同一消息
     */
    public static void sendMoreMessage(WsResponse response, String... uidList) {
        try {
            TextMessage textMessage = new TextMessage(STATIC.objectMapper.writeValueAsString(response));
            for (String uid : uidList) {
                SESSION_MAP.get(uid).sendMessage(textMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
