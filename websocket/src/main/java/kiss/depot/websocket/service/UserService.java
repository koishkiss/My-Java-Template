package kiss.depot.websocket.service;

import kiss.depot.websocket.model.constant.MAPPER;
import kiss.depot.websocket.model.constant.STATIC;
import kiss.depot.websocket.model.dto.UserInfo;
import kiss.depot.websocket.model.enums.CommonErr;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.model.po.AuthPo;
import kiss.depot.websocket.model.vo.response.Response;
import kiss.depot.websocket.util.RedisUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    public Response getUserInfo(Long uid) {
        //uid不可为null
        if (uid == null) {
            return Response.failure(CommonErr.PARAM_WRONG);
        }

        String userInfoKey = RedisKey.USER_INFO.concat(String.valueOf(uid));

        //创建用户信息
        UserInfo userInfo;
        if (RedisUtil.getExpire(userInfoKey) != -2) {
            //先从redis查询用户
            userInfo = RedisUtil.H.getObject(userInfoKey, UserInfo.class);
        } else {
            //找不到就去mysql找
            AuthPo auth = MAPPER.auth.selectInfoByUid(uid);

            //如果mysql中也没有，那么说明该用户不存在
            if (auth == null) {
                return Response.failure(CommonErr.NO_DATA);
            }

            userInfo = new UserInfo(auth);

            //将用户信息缓存至redis
            RedisUtil.H.setObject(userInfoKey, userInfo);
            RedisUtil.setExpire(userInfoKey, STATIC.VALUE.user_info_expire, TimeUnit.MILLISECONDS);
        }

        //返回用户信息
        return Response.success(userInfo);
    }

}
