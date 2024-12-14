package kiss.depot.websocket.model.vo.response;

import kiss.depot.websocket.model.enums.CommonErr;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
* ws的统一返回信息
* 同样code应协商规范
* path中包含指示前端如何操作message中的数据的信息，类似于http中通过url路径来确定如何处理数据
* 常用失败返回请在CommonErr内添加，保证统一性
* author: koishikiss
* launch: 2024/12/8
* last update: 2024/12/15
* */

@Getter
@Setter
@AllArgsConstructor
public class WsResponse {

    private int code;  //返回代码

    private String path;  //返回路径，指示如何操作返回消息

    private Object message;  //返回消息


    //用于被动返回消息

    //ok，用于表示操作成功，适用于前端发送消息时需要等待服务器告知是否发送成功的场景
    public static WsResponse ok() {
        return new WsResponse(200, "/ok", null);
    }

    //ok，用于表示操作成功，适用于前段发送消息时不需要等待返回的场景，当前端接收到返回消息时再处理
    public static WsResponse ok(String path) {
        return new WsResponse(200,path,null);
    }

    //success，用于表示操作成功并返回数据
    public static WsResponse success(String path, Object message) {
        return new WsResponse(200, path, message);
    }

    //failure，适用于前端发送消息时需要等待服务器告知是否发送失败的场景
    public static WsResponse failure(int code) {
        return new WsResponse(code, "/failure", null);
    }

    //failure，适用于前段发送消息时不需要等待返回的场景，当前端接收到返回消息时再处理
    public static WsResponse failure(int code, String path) {
        return new WsResponse(code, path, null);
    }

    //failure，通过commonErr来设置返回错误报告
    public static WsResponse failure(CommonErr commonErr) {
        return new WsResponse(commonErr.getCode(), "/failure", commonErr.getMessage());
    }

    //failure，通过commonErr来设置返回错误报告并指定前端如何处理错误
    public static WsResponse failure(CommonErr commonErr, String path) {
        return new WsResponse(commonErr.getCode(), path, commonErr.getMessage());
    }


    //用于主动返回消息

    public static WsResponse message(String path) {
        return new WsResponse(200,path,null);
    }

    public static WsResponse message(String path, Object message) {
        return new WsResponse(200,path,message);
    }



}
