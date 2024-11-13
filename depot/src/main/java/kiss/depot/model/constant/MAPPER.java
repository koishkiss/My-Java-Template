package kiss.depot.model.constant;

import jakarta.annotation.PostConstruct;
import kiss.depot.mapper.PostMapper;
import kiss.depot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
* Mapper静态注入类
* 统一调度mapper，挺方便的，目前不知道有什么缺点
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

@Component
public class MAPPER {
    @PostConstruct
    @SuppressWarnings("unused")
    public void init() {
        user = userMapper;
        post = postMapper;
    }

    @Autowired
    UserMapper userMapper;
    public static UserMapper user;

    @Autowired
    PostMapper postMapper;
    public static PostMapper post;

}
