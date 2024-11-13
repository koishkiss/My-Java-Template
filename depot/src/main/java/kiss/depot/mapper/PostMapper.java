package kiss.depot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiss.depot.model.po.Post;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {

    void insertAll(List<Post> posts);

    @Delete("DELETE FROM `depot_post` WHERE `id` IN ${idList}")
    void deleteAll(String idList);

    @Select("SELECT MAX(`id`) FROM `depot_post`")
    Integer getMaxId();

    @Select("SELECT COUNT(`id`) FROM `depot_post`")
    Integer count();

    //最新的帖子在后，因此倒序id
    @Select("SELECT * FROM `depot_post` ORDER BY `id` DESC LIMIT #{offset},#{pageSize}")
    List<Post> select(Integer offset, Integer pageSize);



    @Select("SELECT * FROM `depot_post` ORDER BY `id` DESC LIMIT #{pageSize}")
    List<Post> firstSearch(Integer pageSize);

    @Select("SELECT * FROM `depot_post` WHERE `id`>#{startPos} ORDER BY `id` ASC LIMIT #{pageSize}")
    List<Post> searchTowardFront(Integer startPos, Integer pageSize);

    @Select("SELECT * FROM `depot_post` WHERE `id`<#{startPos} ORDER BY `id` DESC LIMIT #{pageSize}")
    List<Post> searchTowardBack(Integer startPos, Integer pageSize);


    //在限制条件下获取数据条数
    @Select("SELECT COUNT(1) FROM (SELECT 1 FROM `depot_post` LIMIT #{scanSize}) AS limit_results")
    Integer getDataNumWithinLimit(Integer scanSize);


}
