package kiss.depot.redis.controller;

import kiss.depot.redis.model.vo.response.Response;
import kiss.depot.redis.util.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseOperatorController {

    @GetMapping("/test/set")
    public Response testForSet() {
        RedisUtil.set("a","b");
        return Response.ok();
    }

}
