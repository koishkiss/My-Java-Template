package kiss.depot.websocket.util;

import kiss.depot.websocket.model.constant.STATIC;
import kiss.depot.websocket.model.dto.request.WsRequest;
import kiss.depot.websocket.model.enums.CommonErr;
import kiss.depot.websocket.model.po.GroupChatPo;
import kiss.depot.websocket.model.po.PrivateChatPo;
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
* last update: 2024/12/9
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
            //解析request
            WsRequest<?> request =  STATIC.objectMapper.readValue(payload, WsRequest.class);

            //根据path参数确定message处理方法
            switch (request.getPath()) {

                //私聊
                case "/chat/private" -> sendOneMessage(
                        websocketService.sendPrivateChat(
                                STATIC.objectMapper.convertValue(
                                        request.getMessage(),
                                        PrivateChatPo.class
                                )
                        ),
                        uid
                );

                //群聊
                case "/chat/group" -> sendOneMessage(
                        websocketService.sendGroupChat(
                                STATIC.objectMapper.convertValue(
                                        request.getMessage(),
                                        GroupChatPo.class
                                )
                        ),
                        uid
                );

                //找不到对应path
                default -> sendOneMessage(WsResponse.failure(CommonErr.RESOURCE_NOT_FOUND), uid);
            }
        } catch (Exception e) {
            //告知发生错误，操作无法正确完成
            sendOneMessage(WsResponse.failure(500,e.getMessage()), uid);
            e.printStackTrace();
        }
    }


    /**
     * 向单个用户发送一条消息
     */
    public static void sendOneMessage(WsResponse response, String uid) {
        try {
            if (response != null)  {
                SESSION_MAP.get(uid).sendMessage(new TextMessage(STATIC.objectMapper.writeValueAsString(response)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向多个用户发送同一消息
     */
    public static void sendMoreMessage(WsResponse response, String... uidList) {
        try {
            if (response != null) {
                TextMessage textMessage = new TextMessage(STATIC.objectMapper.writeValueAsString(response));
                for (String uid : uidList) {
                    SESSION_MAP.get(uid).sendMessage(textMessage);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
