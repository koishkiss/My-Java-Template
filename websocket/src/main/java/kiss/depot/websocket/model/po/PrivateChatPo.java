package kiss.depot.websocket.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @TableId(value = "user_from")
    private Long userFrom;

    @TableId(value = "user_to")
    private Long userTo;

    @TableId(value = "chat_content")
    private String chatContent;

}
