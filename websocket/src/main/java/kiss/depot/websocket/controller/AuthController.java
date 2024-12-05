package kiss.depot.websocket.controller;

import jakarta.annotation.Resource;
import kiss.depot.websocket.annotation.BeforeLogin;
import kiss.depot.websocket.model.po.AuthPo;
import kiss.depot.websocket.model.vo.response.Response;
import kiss.depot.websocket.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Resource
    AuthService authService;

    /**
     * 注册
     */
    @BeforeLogin
    @PostMapping("/register")
    public Response register(@RequestBody AuthPo auth) {
        return authService.register(auth);
    }

    /**
     * 登入
     */
    @BeforeLogin
    @PostMapping("/login")
    public Response login(@RequestBody AuthPo auth) {
        return authService.login(auth);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Response logout(@RequestAttribute(value = "uid") String uid, @RequestAttribute("sessionId") String sessionId) {
        return authService.logout(uid, sessionId);
    }

    /**
     * 注销
     */
    @PostMapping("/logoff")
    public Response logoff() {
        return authService.logoff();
    }

}
