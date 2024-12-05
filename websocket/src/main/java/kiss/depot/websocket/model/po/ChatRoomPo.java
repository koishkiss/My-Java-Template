package kiss.depot.websocket.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@TableName("chat_room")
public class ChatRoomPo {

    @TableId(value = "room_id", type = IdType.ASSIGN_ID)
    private Long roomId;

    @TableField(value = "creator")
    private Long creator;

    @TableField(value = "room_name")
    private String roomName;

    //检查房间名
    public boolean checkRoomNameIsNotValid() {
        return roomName == null || roomName.isBlank();
    }

}



/*
* 表结构：
CREATE TABLE `chat_room` (
  `room_id` bigint NOT NULL COMMENT '房间号id',
  `creator` bigint NOT NULL COMMENT '创建者id',
  `room_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间名',
  PRIMARY KEY (`room_id`),
  KEY `chat_room_room_name_IDX` (`room_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天室';
* */