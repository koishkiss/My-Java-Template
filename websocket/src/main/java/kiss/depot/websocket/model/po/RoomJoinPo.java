package kiss.depot.websocket.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("room_join")
public class RoomJoinPo {

    @TableField(value = "uid")
    private Long uid;

    @TableField(value = "room_id")
    private Long roomId;

}


/*
* 表结构：
CREATE TABLE `room_join` (
  `uid` bigint NOT NULL COMMENT '用户id',
  `roomId` bigint NOT NULL COMMENT '房间id',
  PRIMARY KEY (`uid`,`roomId`),
  KEY `room_join_roomId_IDX` (`roomId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天室加入情况';
* */