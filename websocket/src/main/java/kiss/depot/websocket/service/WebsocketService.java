package kiss.depot.websocket.service;

import kiss.depot.websocket.model.dto.request.WsRequest;
import kiss.depot.websocket.model.vo.response.WsResponse;
import org.springframework.stereotype.Service;

/*
* ws消息处理
* author: koishikiss
* launch: 2024/12/8
* last update: 2024/12/8
* */

@Service
public class WebsocketService {

    public WsResponse test(WsRequest<String> request) {
        return WsResponse.success(request.getPath(),request.getMessage());
    }

}
