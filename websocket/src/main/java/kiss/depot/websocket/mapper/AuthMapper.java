package kiss.depot.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiss.depot.websocket.model.po.AuthPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper extends BaseMapper<AuthPo> {

    @Select("SELECT EXISTS(SELECT 1 FROM `user_auth` WHERE `nickname`=#{nickname})")
    boolean judgeExistsOfNickname(String nickname);

    @Select("SELECT `uid`,`nickname`,`password` FROM `user_auth` WHERE `nickname`=#{nickname}")
    AuthPo selectByNickname(String nickname);

    @Select("SELECT `uid`,`nickname` FROM `user_auth` WHERE `uid`=#{uid}")
    AuthPo selectInfoByUid(Long uid);

}
