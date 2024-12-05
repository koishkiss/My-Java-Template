package kiss.depot.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiss.depot.websocket.model.po.ChatRoomPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChatRoomMapper extends BaseMapper<ChatRoomPo> {

    @Select("SELECT EXISTS(SELECT 1 FROM `chat_room` WHERE `room_name`=#{roomName})")
    boolean judgeExistsOfRoomName(String roomName);

}
