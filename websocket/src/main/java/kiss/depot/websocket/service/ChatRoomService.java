package kiss.depot.websocket.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import kiss.depot.websocket.model.constant.MAPPER;
import kiss.depot.websocket.model.enums.CommonErr;
import kiss.depot.websocket.model.po.ChatRoomPo;
import kiss.depot.websocket.model.po.RoomJoinPo;
import kiss.depot.websocket.model.vo.response.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class ChatRoomService {

    //创建房间锁
    ReentrantLock createRoomLock = new ReentrantLock();

    //创建聊天室
    @Transactional
    public Response createChatRoom(Long uid, ChatRoomPo newChatRoom) {

        //检查房间信息填写是否正确
        if (newChatRoom.checkRoomNameIsNotValid()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("房间名不可为空!"));
        }

        //设置房间初始化信息
        newChatRoom.setCreator(uid);
        newChatRoom.setRoomId(null);

        //使用悲观锁
        createRoomLock.lock();
        try {
            //考察是否已经有这个房间名
            if (MAPPER.chat_room.judgeExistsOfRoomName(newChatRoom.getRoomName())) {
                //告知房间名已存在
                return Response.failure(400,"该房间名已被占用！");
            } else {
                //创建房间
                MAPPER.chat_room.insert(newChatRoom);
                //将创建者加入房间
                MAPPER.room_join.insert(new RoomJoinPo(uid, newChatRoom.getRoomId()));
                //告知创建成功
                return Response.ok();
            }
        } finally {
            //解锁
            createRoomLock.unlock();
        }
    }

    //获取当前存在的聊天室
    public Response getAllChatRoom() {
        return Response.success(MAPPER.chat_room.selectList(new QueryWrapper<>()));
    }

    //加入聊天室
    public Response joinChatRoom() {
        return null;
    }

    //退出聊天室
    public Response quitChatRoom() {
        return null;
    }

    //删除聊天室
    public Response removeChatRoom() {
        return null;
    }

}
