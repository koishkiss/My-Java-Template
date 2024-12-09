package kiss.depot.websocket.service;

import kiss.depot.websocket.model.po.GroupChatPo;
import kiss.depot.websocket.model.po.PrivateChatPo;
import kiss.depot.websocket.model.vo.response.WsResponse;
import org.springframework.stereotype.Service;

/*
* ws消息处理
* author: koishikiss
* launch: 2024/12/8
* last update: 2024/12/8
* */

@Service
public class WebsocketService {

    // 发布私聊消息
    public WsResponse sendPrivateChat(PrivateChatPo newPrivateChat) {
        return WsResponse.success("/ok",newPrivateChat);
    }

    // 发布群聊消息
    public WsResponse sendGroupChat(GroupChatPo newGroupChat) {
        return WsResponse.success("/ok",newGroupChat);
    }

}
