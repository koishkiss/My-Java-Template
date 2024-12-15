package kiss.depot.websocket.service;

import kiss.depot.websocket.model.constant.MAPPER;
import kiss.depot.websocket.model.dto.messageQueue.ForceLogoutCheckInfo;
import kiss.depot.websocket.model.dto.messageQueue.UserMessageQueueElement;
import kiss.depot.websocket.model.enums.CommonErr;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.model.po.GroupChatPo;
import kiss.depot.websocket.model.po.PrivateChatPo;
import kiss.depot.websocket.model.vo.response.WsResponse;
import kiss.depot.websocket.util.RedisUtil;
import kiss.depot.websocket.util.WebsocketUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

/*
* ws消息处理
* author: koishikiss
* launch: 2024/12/8
* last update: 2024/12/15
* */

@Service
public class WebsocketService {

    //--------------------- 处理前端请求消息 ---------------------//

    // 发布私聊消息
    public WsResponse sendPrivateChat(PrivateChatPo newPrivateChat, String uid) {
        //对消息进行一定处理
        if (newPrivateChat.checkContentIsIllegal()) {
            return WsResponse.failure(CommonErr.PARAM_WRONG.setMsg("不可发布空内容!"));
        }
        if (newPrivateChat.checkUserToIsIllegal()) {
            return WsResponse.failure(CommonErr.PARAM_WRONG.setMsg("找不到该用户!"));
        }
        if (uid.equals(newPrivateChat.getUserTo().toString())) {
            return WsResponse.failure(CommonErr.PARAM_WRONG.setMsg("不可向自己发送消息!"));
        }

        newPrivateChat.setChatId(null);
        newPrivateChat.setUserFrom(Long.valueOf(uid));

        //将消息存储进mysql
        MAPPER.private_chat.insert(newPrivateChat);

        //将消息存入对方的消息队列（从右侧放入）
        RedisUtil.L.rightPushObject(
                RedisKey.USER_MESSAGE_LIST.concat(String.valueOf(newPrivateChat.getUserTo())),
                new UserMessageQueueElement<>("privateChat",newPrivateChat)
        );

        //返回发送成功提示和处理后消息状态
        return WsResponse.success("/private/chat/success",newPrivateChat);
    }

    // 发布群聊消息
    public WsResponse sendGroupChat(GroupChatPo newGroupChat) {
        return WsResponse.success("/ok",newGroupChat);
    }

    //--------------------- 处理消息队列消息 ---------------------//

    //接收到强制下线命令
    public void receiveForceLogout(ForceLogoutCheckInfo checkInfo, String uid, String websocketSessionId) {
        //检查命令是否过期
        if (checkInfo.checkUnexpired()) {
            try {
                //检查命令是否匹对
                if (checkInfo.checkMatched(websocketSessionId)) {
                    WebsocketUtil.sendOneMessage(WsResponse.message("/force/logout"), websocketSessionId);
                    WebsocketUtil.SESSION_MAP.get(websocketSessionId).close();
                } else {
                    //将命令放回队列
                    RedisUtil.L.leftPushObject(
                            RedisKey.USER_MESSAGE_LIST.concat(uid),
                            new UserMessageQueueElement<>("forceLogout", checkInfo));
                    //损失500毫秒性能来保证能将消息传递给正确的设备
                    Thread.sleep(500);
                }
            } catch (IOException | InterruptedException ignored) {}
        }
    }

    //接收到私聊消息
    public WsResponse receivePrivateChat(PrivateChatPo receivedChat) {
        return WsResponse.message("/private/chat/receive",receivedChat);
    }

}
