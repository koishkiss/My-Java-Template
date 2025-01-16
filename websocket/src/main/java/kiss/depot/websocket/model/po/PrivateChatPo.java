package kiss.depot.websocket.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import kiss.depot.websocket.model.constant.MAPPER;
import kiss.depot.websocket.model.dto.UserInfo;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.util.RedisUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@TableName("private_chat_record")
public class PrivateChatPo {

    @TableId(value = "chat_id", type = IdType.ASSIGN_ID)
    private Long chatId;

    @TableField(value = "user_from")
    private Long userFrom;

    @TableField(value = "user_to")
    private Long userTo;

    @TableField(value = "chat_content")
    private String chatContent;

    //检查聊天内容是否非法，可添加更多逻辑
    public boolean checkContentIsIllegal() {
        return chatContent == null || chatContent.isBlank();
    }

    //检查userTo数据是否非法
    public boolean checkUserToIsIllegal() {
        if (userTo == null) {
            return true;
        }

        //先从redis确定用户是否存在
        Long expire = RedisUtil.getExpire(RedisKey.USER_INFO.concat(userTo.toString()));
        //存在即返回不非法
        if (expire != null && expire != -2) {
            return false;
        }
        //否则去mysql查找用户
        else {
            AuthPo user = MAPPER.auth.selectInfoByUid(userTo);
            //mysql也找不到该用户，表明用户不存在，返回非法
            if (user == null) {
                return true;
            } else {
                //存在则将用户信息存入redis，返回不非法
                RedisUtil.H.setObject(RedisKey.USER_INFO.concat(userTo.toString()), new UserInfo(user));
                return false;
            }
        }
    }

}



/*
* 表结构：
CREATE TABLE `private_chat_record` (
  `chat_id` bigint NOT NULL COMMENT '记录编号',
  `user_from` bigint NOT NULL COMMENT '发起用户',
  `user_to` bigint NOT NULL COMMENT '接收用户',
  `chat_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`chat_id`),
  KEY `private_chat_record_user_to_IDX` (`user_to`,`user_from`) USING BTREE,
  KEY `private_chat_record_user_from_IDX` (`user_from`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='私聊记录';
* */
