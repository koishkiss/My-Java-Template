package kiss.depot.websocket.config.webMvcConfig;

import io.jsonwebtoken.Claims;
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

    public static final String Token_IN_HEADER = "Authorization";

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        //打印请求路径
        log.info("收到新的请求 " + request.getServletPath() + "\n");

        if (handler instanceof HandlerMethod handlerMethod) {
            //检查请求的接口是否需要登入
            if (handlerMethod.getMethodAnnotation(BeforeLogin.class) == null) {
                //获取请求头中携带的token
                String token = request.getHeader(Token_IN_HEADER);
                if (token == null) {
                    throw new TokenException("请登入!");
                }

                //获取token并解析出claims
                Claims claims = JwtUtil.jwt.getClaim(token);
                if (claims == null) {
                    throw new TokenException("请重新登入!");
                }

                //从claims中提取出uid和sessionId
                String uid = String.valueOf(claims.get(JwtUtil.CLAIM_UID));
                String sessionId = String.valueOf(claims.get(JwtUtil.CLAIM_SESSION_ID));
                if (uid == null || sessionId == null) {
                    throw new TokenException("请重新登入!");
                }

                //从redis中获取uid对应的session
                String storedSessionId = RedisUtil.S.get(RedisKey.USER_SESSION.concat(uid));

                //storedSessionId为null表示键已过期或已被删除，sessionId与之对应不上则表明token过期
                if (storedSessionId == null || !storedSessionId.equals(sessionId)) {
                    throw new TokenException("登入已过期，请重新登入!");
                }

                //将uid存入请求的Attribute标注请求发起者
                request.setAttribute("uid", uid);
                request.setAttribute("sessionId",sessionId);
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
