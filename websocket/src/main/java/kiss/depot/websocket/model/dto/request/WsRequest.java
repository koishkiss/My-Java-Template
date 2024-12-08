package kiss.depot.websocket.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
* ws的统一请求信息
* path中包含指示服务器如何操作message中的数据的信息，类似于http中通过url路径来确定如何处理数据
* author: koishikiss
* launch: 2024/12/8
* last update: 2024/12/8
* */

@Getter
@Setter
@NoArgsConstructor
public class WsRequest<T> {

    private String path;  //请求路径

    private T message;  //请求数据

}
