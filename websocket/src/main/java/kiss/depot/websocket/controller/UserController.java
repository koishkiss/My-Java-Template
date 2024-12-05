package kiss.depot.websocket.controller;

import jakarta.annotation.Resource;
import kiss.depot.websocket.model.vo.response.Response;
import kiss.depot.websocket.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     * 获取用户自己信息
     */
    @GetMapping("/info/mine")
    public Response getMyInfo(@RequestAttribute(value = "uid") Long uid) {
        return userService.getUserInfo(uid);
    }

    /**
     * 获取其它用户信息
     */
    @GetMapping("/info/other")
    public Response getOtherInfo(@RequestParam(value = "uid") Long uid) {
        return userService.getUserInfo(uid);
    }

    /**
     * 获取在线用户
     */
    @GetMapping("/user/online")
    public Response getUserOnline() {
        return userService.getUserOnline();
    }


}
