package kiss.depot.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiss.depot.websocket.model.po.RoomJoinPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomJoinMapper extends BaseMapper<RoomJoinPo> {
}
