package kiss.depot.websocket.service;

import kiss.depot.websocket.model.constant.MAPPER;
import kiss.depot.websocket.model.constant.STATIC;
import kiss.depot.websocket.model.dto.UserInfo;
import kiss.depot.websocket.model.enums.CommonErr;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.model.po.AuthPo;
import kiss.depot.websocket.model.vo.response.Response;
import kiss.depot.websocket.util.JwtUtil;
import kiss.depot.websocket.util.RedisUtil;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AuthService {

    //注册锁
    private final ReentrantLock registerLock = new ReentrantLock();

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

        //生成UUID作为session
        String session = String.valueOf(UUID.randomUUID());

        //将账号session存储进redis
        RedisUtil.S.VALUE.set(
                RedisKey.USER_SESSION.concat(session),
                String.valueOf(user.getUid()),
                STATIC.VALUE.jwt_expire,
                TimeUnit.MILLISECONDS
        );

        //创建用户信息
        UserInfo userInfo = new UserInfo(user);

        //将用户信息存储进redis
        String userInfoKey = RedisKey.USER_INFO.concat(String.valueOf(user.getUid()));
        RedisUtil.H.setObject(userInfoKey, userInfo);

        //设置用户信息缓存时间
        RedisUtil.setExpire(userInfoKey, STATIC.VALUE.user_info_expire, TimeUnit.MILLISECONDS);

        //将session打包进jwt后连带账号信息一起返回
        @Getter
        class UserDto {
            private final String token;
            private final UserInfo user;

            public UserDto(String session, UserInfo userInfo) {
                token = JwtUtil.token.generate(session);
                user = userInfo;
            }
        }

        return Response.success(new UserDto(session, userInfo));
    }

    //登出
    public Response logout(String session) {
        //清理用户的websocketSession

        //清理redis里的用户信息
        if (RedisUtil.delete(RedisKey.USER_SESSION.concat(session))) {
            return Response.ok();
        } else {
            return Response.failure(400,"操作失败!");
        }
    }

    //注销
    public Response logoff() {
        return null;
    }

}
