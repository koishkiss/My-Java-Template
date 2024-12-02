package kiss.depot.websocket.config.exceptionConfig.exceptions;


import kiss.depot.websocket.config.exceptionConfig.ExceptionReturn;
import kiss.depot.websocket.model.enums.CommonErr;
import kiss.depot.websocket.model.vo.response.Response;

public class CommonErrException extends RuntimeException implements ExceptionReturn {
    protected final CommonErr ERROR;

    public CommonErrException(CommonErr err) {
        ERROR = err;
    }

    //静态构造器
    public static CommonErrException raise(CommonErr err) {
        return new CommonErrException(err);
    }

    @Override
    public String getMessage() {
        return ERROR.getMessage();
    }

    @Override
    public Response toResponse() {
        return Response.failure(ERROR);
    }
}
