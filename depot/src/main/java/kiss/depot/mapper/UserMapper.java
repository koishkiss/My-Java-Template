package kiss.depot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiss.depot.model.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

//    @Select("INSERT INTO `depot_user`(" +
//                "`sid`," +
//                "`password`," +
//                "`name`," +
//                "`nickname`" +
//            ") " +
//            "VALUES (" +
//                "#{sid}," +
//                "#{password}," +
//                "#{name}," +
//                "CONCAT('u_',#{sid})" +
//            ");" +
//            "SELECT * FROM `user` WHERE `sid`=#{sid}"
//    )
//    User register(String sid,String password,String name);

    @Select("SELECT * FROM `depot_user` WHERE `sid`=#{sid}")
    User getUserBySid(String sid);

}
