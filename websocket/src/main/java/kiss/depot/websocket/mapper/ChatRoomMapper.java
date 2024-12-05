package kiss.depot.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiss.depot.websocket.model.po.ChatRoomPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChatRoomMapper extends BaseMapper<ChatRoomPo> {

    @Override
    @Insert("INSERT INTO `chat_room`(`creator`,`room_name`) VALUES(#{creator},#{roomName})")
    @Options(useGeneratedKeys = true, keyColumn = "room_id", keyProperty = "roomId")
    int insert(ChatRoomPo chatRoomPo);

    @Select("SELECT EXISTS(SELECT 1 FROM `chat_room` WHERE `room_name`=#{roomName})")
    boolean judgeExistsOfRoomName(String roomName);

}
