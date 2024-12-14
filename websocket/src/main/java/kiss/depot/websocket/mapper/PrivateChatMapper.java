package kiss.depot.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiss.depot.websocket.model.po.PrivateChatPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrivateChatMapper extends BaseMapper<PrivateChatPo> {
}
