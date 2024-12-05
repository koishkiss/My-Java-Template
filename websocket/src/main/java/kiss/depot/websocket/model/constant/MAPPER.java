package kiss.depot.websocket.model.constant;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import kiss.depot.websocket.mapper.AuthMapper;
import kiss.depot.websocket.mapper.ChatRoomMapper;
import kiss.depot.websocket.mapper.RoomJoinMapper;
import org.springframework.stereotype.Component;

@Component
public class MAPPER {

    @PostConstruct
    @SuppressWarnings("unused")
    public void init() {
        auth = authMapper;
        chat_room = chatRoomMapper;
        room_join = roomJoinMapper;
    }

    @Resource
    AuthMapper authMapper;
    public static AuthMapper auth;

    @Resource
    ChatRoomMapper chatRoomMapper;
    public static ChatRoomMapper chat_room;

    @Resource
    RoomJoinMapper roomJoinMapper;
    public static RoomJoinMapper room_join;

}
