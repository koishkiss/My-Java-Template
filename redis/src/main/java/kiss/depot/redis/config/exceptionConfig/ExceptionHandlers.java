package kiss.depot.redis.config.exceptionConfig;

import com.fasterxml.jackson.databind.JsonMappingException;
import kiss.depot.redis.config.exceptionConfig.exceptions.CommonErrException;
import kiss.depot.redis.config.exceptionConfig.exceptions.ParamCheckException;
import kiss.depot.redis.config.exceptionConfig.exceptions.TokenException;
import kiss.depot.redis.model.enums.CommonErr;
import kiss.depot.redis.model.vo.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/*
* 进行一个错误的全局捕获
* 目前写成这样能实现功能但有些难看，后续优化
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    //非自定义错误抛出

//    //Mybatis抛出数据库连接异常
//    @ExceptionHandler(MyBatisSystemException.class)
//    public Response error(MyBatisSystemException e) {
//        System.out.println("连接到数据库异常!");
//        e.printStackTrace();
//        return Response.failure(CommonErr.CONNECT_TO_MYSQL_FAILED);
//    }

    //spring-web抛出超出最大上传数据异常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Response error(MaxUploadSizeExceededException e) {
        System.out.println(e.getMessage());
        return Response.failure(CommonErr.FILE_OUT_OF_LIMIT);
    }

    //spring-http抛出http不可读异常，可能包含参数异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response error(HttpMessageNotReadableException e) {
        System.out.println(e.getMessage());
        if (e.getCause() != null && e.getCause() instanceof JsonMappingException jsonMappingException) {
            if (jsonMappingException.getCause() != null && jsonMappingException.getCause() instanceof ParamCheckException paramCheckException) {
                System.out.println(paramCheckException.getMessage());
                return paramCheckException.toResponse();
            }
        }
        e.printStackTrace();
        return Response.failure(CommonErr.PARAM_WRONG);
    }


    //自定义错误抛出

    //参数传入错误异常
    @ExceptionHandler(ParamCheckException.class)
    public Response error(ParamCheckException e) {
        System.out.println(e.getMessage());
        return e.toResponse();
    }

    //token解析异常
    @ExceptionHandler(TokenException.class)
    public Response error(TokenException e) {
        System.out.println(e.getMessage());
        return e.toResponse();
    }

    //普通错误异常
    @ExceptionHandler(CommonErrException.class)
    public Response error(CommonErrException e) {
        System.out.println(e.getMessage());
        return e.toResponse();
    }

    //其它异常抛出

    @ExceptionHandler(RuntimeException.class)
    public Response error(RuntimeException e) {
        e.printStackTrace();
        return Response.failure(401, String.valueOf(e));
    }

    @ExceptionHandler(Exception.class)
    public Response error(Exception e) {
        e.printStackTrace();
        return Response.failure(402, String.valueOf(e));
    }

}
