package kiss.depot.redis.model.vo.response;

import kiss.depot.redis.model.enums.CommonErr;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
* 统一返回信息
* code应协商规范
* 常用失败返回请在CommonErr内添加，保证统一性
* 这里data不使用泛型，主要是为了方便。这个类就是为了方便服务的。
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private int code;  //返回代码

    private String message;  //返回信息

    private Object data;  //返回数据

    //ok:成功
    public static Response ok() {  //告知响应成功
        return new Response(200,"ok",null);
    }

    //success:成功且返回单个数据
    public static Response success(Object data) {  //返回响应参数
        return new Response(200,"success",data);
    }


    //failure:失败且无返回
    public static Response failure(int code, String msg) {  //告知操作失败，并返回原因
        return new Response(code, msg,null);
    }

    //常用失败返回
    public static Response failure(CommonErr commonErr) {
        return new Response(commonErr.getCode(),commonErr.getMessage(),null);
    }

}
