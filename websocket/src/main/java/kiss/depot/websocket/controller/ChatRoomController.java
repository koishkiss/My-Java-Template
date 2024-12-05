package kiss.depot.websocket.controller;

import jakarta.annotation.Resource;
import kiss.depot.websocket.annotation.BeforeLogin;
import kiss.depot.websocket.model.po.ChatRoomPo;
import kiss.depot.websocket.model.vo.response.Response;
import kiss.depot.websocket.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatRoomController {

    @Resource
    ChatRoomService chatRoomService;

    /**
     * 新建聊天室
     */
    @PostMapping("/chat/create")
    public Response createChatRoom(@RequestAttribute(value = "uid") Long uid, @RequestBody ChatRoomPo newChatRoom) {
        return chatRoomService.createChatRoom(uid, newChatRoom);
    }

    /**
     * 获取当前存在的聊天室
     */
    @BeforeLogin
    @GetMapping("/chat/get")
    public Response getAllChatRoom() {
        return chatRoomService.getAllChatRoom();
    }

    /**
     * 加入聊天室
     */
    @PutMapping("/chat/join")
    public Response joinChatRoom(@RequestAttribute(value = "uid") Long uid, @RequestParam(value = "roomId") Long roomId) {
        return chatRoomService.joinChatRoom();
    }

    /**
     * 退出聊天室
     */
    @PutMapping("/chat/quit")
    public Response quitChatRoom(@RequestAttribute(value = "uid") Long uid, @RequestParam(value = "roomId") Long roomId) {
        return chatRoomService.quitChatRoom();
    }

    /**
     * 删除聊天室
     */
    @DeleteMapping("/chat/remove")
    public Response removeChatRoom(@RequestAttribute(value = "uid") Long uid, @RequestParam(value = "roomId") Long roomId) {
        return chatRoomService.removeChatRoom();
    }

}
