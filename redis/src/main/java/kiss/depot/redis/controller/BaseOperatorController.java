package kiss.depot.redis.controller;

import kiss.depot.redis.model.constant.REDIS_KEY.USER;
import kiss.depot.redis.model.po.User;
import kiss.depot.redis.model.vo.response.Response;
import kiss.depot.redis.util.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseOperatorController {

    @GetMapping("/test/set")
    public Response testForSet() {
        RedisUtil.S.set("a","b");
        return Response.success(RedisUtil.S.get("a"));
    }

    @GetMapping("/test/set/object")
    public Response testForSetObject() {
        User user = new User("514", "koishi", "415");
        
        System.out.println(user);

        RedisUtil.S.setObject(USER.INFO.get(user.getUid()), user);

        User user1 = RedisUtil.S.getObject(USER.INFO.get(user.getUid()));

        System.out.println(user1);

        return Response.success(user1);
    }

}
