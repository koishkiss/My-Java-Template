package kiss.depot.websocket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
* 用户消息队列中元素的结构
* type中包含如何处理该消息的信息
* author: koishikiss
* launch: 2024/12/14
* last update: 2024/12/14
* */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageQueueElement<T> {

    private String type;  //消息类型

    private T message;  //消息数据

}
