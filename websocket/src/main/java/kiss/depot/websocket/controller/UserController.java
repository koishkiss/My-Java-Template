package kiss.depot.websocket.controller;

import jakarta.annotation.Resource;
import kiss.depot.websocket.model.vo.response.Response;
import kiss.depot.websocket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/info/mine")
    public Response getMyInfo(@RequestAttribute(value = "uid") Long uid) {
        return userService.getUserInfo(uid);
    }

    @GetMapping("/info/other")
    public Response getOtherInfo(@RequestParam(value = "uid") Long uid) {
        return userService.getUserInfo(uid);
    }

}
