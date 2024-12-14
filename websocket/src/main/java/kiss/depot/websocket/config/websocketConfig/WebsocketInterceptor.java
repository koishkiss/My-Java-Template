package kiss.depot.websocket.config.websocketConfig;

import io.jsonwebtoken.Claims;
import kiss.depot.websocket.config.exceptionConfig.exceptions.TokenException;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.util.JwtUtil;
import kiss.depot.websocket.util.RedisUtil;
import kiss.depot.websocket.util.WebsocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

import static kiss.depot.websocket.config.webMvcConfig.Interceptor.Token_IN_HEADER;

/*
* ws连接处理
* 用于处理ws握手前后的访问控制操作
* author: koishikiss
* launch: 2024/12/2
* last update: 2024/12/14
* */

@Slf4j
public class WebsocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response,
            @NotNull WebSocketHandler wsHandler,
            @NotNull Map<String, Object> attributes) {

        //打印请求
        log.info("收到新的长连接请求\n");

        //获取请求头中携带的token
        List<String> tokens = request.getHeaders().get(Token_IN_HEADER);
        if (tokens == null || tokens.isEmpty()) {
            throw new TokenException("请登入!");
        }

        //获取token并解析出claims
        Claims claims = JwtUtil.jwt.getClaim(tokens.get(0));
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

        //已经是在线状态，禁止重复上线
        if (RedisUtil.getExpire(RedisKey.USER_ONLINE.concat(uid)) != -2) {
            throw new TokenException("重复的操作!");
        }

        //将uid存入请求的Attribute标注长连接发起者
        attributes.put("uid", uid);
        attributes.put("websocketSessionId", WebsocketUtil.generatorWebsocketSessionId(uid, sessionId));
        return true;
    }

    @Override
    public void afterHandshake(
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response,
            @NotNull WebSocketHandler wsHandler,
            Exception exception) {
        log.info("ws握手后\n");
    }
}
