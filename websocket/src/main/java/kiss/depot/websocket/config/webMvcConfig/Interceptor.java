package kiss.depot.websocket.config.webMvcConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kiss.depot.websocket.annotation.BeforeLogin;
import kiss.depot.websocket.config.exceptionConfig.exceptions.TokenException;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.util.JwtUtil;
import kiss.depot.websocket.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/*
* 拦截器设置
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

@Slf4j
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        //打印请求路径
        log.info("一条新的请求 " + request.getServletPath());
        System.out.println(request.getAttribute("user"));

        if (handler instanceof HandlerMethod handlerMethod) {
            //检查请求的接口是否需要登入
            if (handlerMethod.getMethodAnnotation(BeforeLogin.class) == null) {
                //获取请求头中携带的token
                String token = request.getHeader("Authorization");
                if (token == null) {
                    throw new TokenException("请登入!");
                }

                //获取token并提取出session
                String session = JwtUtil.token.getClaim(token);
                if (session == null) {
                    throw new TokenException("请重新登入!");
                }

                //从redis中获取session对应的uid
                String uid = RedisUtil.S.get(RedisKey.USER_SESSION.concat(session));

                //uid为null表示键已过期或已被删除
                if (uid == null) {
                    throw new TokenException("请重新登入!");
                }

                //将uid和session存入请求的Attribute
                request.setAttribute("uid", uid);
                request.setAttribute("session", session);
            }
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request,
                           @NotNull HttpServletResponse response,
                           @NotNull Object handler,
                           ModelAndView modelAndView) {
        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler,
                                Exception ex) {
        System.out.println("afterCompletion");
    }
}


/*
* using dependency:
<!--jetbrains依赖包(主要用于@Nullable注解等)-->
<dependency>
    <groupId>org.jetbrains</groupId>
    <artifactId>annotations</artifactId>
    <version>13.0</version>
</dependency>
* */
