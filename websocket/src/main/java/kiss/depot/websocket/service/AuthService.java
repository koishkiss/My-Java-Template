package kiss.depot.websocket.service;

import kiss.depot.websocket.model.constant.MAPPER;
import kiss.depot.websocket.model.constant.STATIC;
import kiss.depot.websocket.model.dto.UserInfo;
import kiss.depot.websocket.model.enums.CommonErr;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.model.po.AuthPo;
import kiss.depot.websocket.model.vo.response.Response;
import kiss.depot.websocket.util.JwtUtil;
import kiss.depot.websocket.util.RandomUtil;
import kiss.depot.websocket.util.RedisUtil;
import kiss.depot.websocket.util.WebsocketUtil;
import lombok.Getter;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AuthService {

    //注册锁
    private final ReentrantLock registerLock = new ReentrantLock();

    private static final String userAuthLock = "/user/auth/";

    //注册
    public Response register(AuthPo auth) {

        //检查昵称
        if (auth.checkNicknameIsNotValid()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("昵称不可为空!"));
        }

        //检查密码
        if (auth.checkPasswordIsNotValid()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("密码不可为空!"));
        }

        // 加密密码
        auth.encryptPassword();

        //设置uid为自动生成
        auth.setUid(null);

        //使用悲观锁
        registerLock.lock();
        try {
            //检查昵称是否已经存在
            if (MAPPER.auth.judgeExistsOfNickname(auth.getNickname())) {
                //告知用户昵称已存在
                return Response.failure(400,"昵称已存在!");
            } else {
                //创建用户
                MAPPER.auth.insert(auth);
                //返回成功信息
                return Response.ok();
            }
        } finally {
            //解锁
            registerLock.unlock();
        }
    }

    //登入
    public Response login(AuthPo auth) {
        // 先进行登入检查

        //检查昵称
        if (auth.checkNicknameIsNotValid()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("昵称不可为空!"));
        }

        //检查密码
        if (auth.checkPasswordIsNotValid()) {
            return Response.failure(CommonErr.PARAM_WRONG.setMsg("密码不可为空!"));
        }

        //根据昵称获取账号信息
        AuthPo user = MAPPER.auth.selectByNickname(auth.getNickname());

        //检查该账号是否存在
        if (user == null) {
            return Response.failure(400,"账号不存在!");
        }

        //检查密码是否正确
        if (!auth.validatePassword(user.getPassword())) {
            return Response.failure(400,"密码错误!");
        }

        // 登入检验通过，下面的步骤执行登入操作

        //对各个用户的登入操作加锁
        synchronized ((userAuthLock + auth.getUid()).intern()) {
            //检查用户在别的地方是否有上线操作，有则需要中断在其它地方的长连接，当然也可以因为其它地方正在长连接而阻止该端口登入
            //在这里仅允许单点上线的设计是合理的。如果有需求可以将手机端和电脑端的sessionId和长连接分开设置，实现双端登入上线状态分离
            //还有一种实现，就是前端在执行登入接口前先查询是否存在其它地方已经上线的情况，如果存在就先询问再让用户确定是否登入
            String userSessionKey = RedisKey.USER_SESSION.concat(String.valueOf(user.getUid()));
            //获取用户的旧sessionId
            String oldSession = RedisUtil.S.get(userSessionKey);
            if (oldSession != null && RedisUtil.getExpire(RedisKey.USER_ONLINE.concat(String.valueOf(user.getUid()))) != -2) {
                //向该用户的消息队列推送登出信息，要求该用户在其它客户端进行的长连接下线
                // TODO: 下线其它地方的长连接
            }

            //生成随机code作为新session，与旧session不重复
            String newSession = RandomUtil.generateRandomCode(4, oldSession);

            //将账号session存储进redis
            RedisUtil.S.VALUE.set(
                    userSessionKey,
                    newSession,
                    STATIC.VALUE.jwt_expire,
                    TimeUnit.MILLISECONDS
            );

            //创建用户信息
            UserInfo userInfo = new UserInfo(user);

            RedisUtil.redis.execute((RedisCallback<Object>) connection -> {
                //将用户信息存储进redis
                String userInfoKey = RedisKey.USER_INFO.concat(String.valueOf(user.getUid()));
                RedisUtil.H.setObject(userInfoKey, userInfo);

                //设置用户信息缓存时间
                RedisUtil.setExpire(userInfoKey, STATIC.VALUE.user_info_expire, TimeUnit.MILLISECONDS);

                return null;
            });

            //将session打包进jwt后连带账号信息一起返回
            @Getter
            class UserDto {
                private final String token;
                private final UserInfo user;

                public UserDto(String session, UserInfo userInfo) {
                    token = JwtUtil.jwt.generate(userInfo.getUid(), session);
                    user = userInfo;
                }
            }

            return Response.success(new UserDto(newSession, userInfo));
        }
    }

    //登出
    public Response logout(String uid, String sessionId) {
        synchronized ((userAuthLock + uid).intern()) {
            //下线并清理用户的websocketSession
            try {
                String websocketSessionId = WebsocketUtil.generatorWebsocketSessionId(uid, sessionId);
                WebSocketSession webSocketSession = WebsocketUtil.SESSION_MAP.get(websocketSessionId);
                if (webSocketSession != null) {
                    webSocketSession.close();
                    WebsocketUtil.SESSION_MAP.remove(websocketSessionId);
                }
            } catch (IOException e) {
                return Response.failure(400,"操作失败,请尝试先设置为离线状态!");
            }

            //检查redis里的sessionId是否对应的上
            String storedSessionId = RedisUtil.S.get(RedisKey.USER_SESSION.concat(uid));
            if (storedSessionId == null) {
                //未获取到sessionId，可能登入已过期
                return Response.ok();
            }
            if (!storedSessionId.equals(sessionId)) {
                //对应不上，表明此时可能此时在试图下线别的地方刚登入时使用的sessionId
                return Response.failure(400, "操作失败!");
            }

            //清理redis里的用户session
            if (RedisUtil.delete(RedisKey.USER_SESSION.concat(uid))) {
                return Response.ok();
            } else {
                return Response.failure(400, "操作失败!");
            }
        }
    }

    //注销
    public Response logoff() {
        return null;
    }

}
