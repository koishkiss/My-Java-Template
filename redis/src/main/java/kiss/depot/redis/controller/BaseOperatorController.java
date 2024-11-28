package kiss.depot.redis.controller;

import kiss.depot.redis.model.constant.REDIS_KEY.USER;
import kiss.depot.redis.model.po.User;
import kiss.depot.redis.model.vo.response.Response;
import kiss.depot.redis.util.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

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

        RedisUtil.S.setObject(USER.INFO.join(user.getUid()), user);

        User user1 = RedisUtil.S.getObject(USER.INFO.join(user.getUid()), user.getClass());

        System.out.println(user1);

        return Response.success(user1);
    }

    @GetMapping("/test/expire")
    public Response testForSetExpire() {

        RedisUtil.delete("c");

        boolean c = RedisUtil.setExpire("c",100);
        System.out.println(c);
        System.out.println(RedisUtil.getExpire("c"));


        RedisUtil.S.set("b","111");

        boolean b = RedisUtil.setExpire("b",100);

        if (b) {
            System.out.println((RedisUtil.getExpire("b")));
        }

        RedisUtil.S.set("d","123");
        RedisUtil.S.set("e","123");

        System.out.println(RedisUtil.delete(List.of(new String[]{"c", "d", "e"})));

        return Response.ok();
    }

}
