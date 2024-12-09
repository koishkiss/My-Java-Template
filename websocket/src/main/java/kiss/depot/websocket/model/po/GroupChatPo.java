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
@TableName("group_chat_record")
public class GroupChatPo {

    @TableId(value = "chat_id", type = IdType.ASSIGN_ID)
    private Long chatId;

    @TableField(value = "room_id")
    private Long roomId;

    @TableField(value = "user_from")
    private Long userFrom;

    @TableField(value = "chat_content")
    private String chatContent;

}
