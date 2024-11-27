package kiss.depot.redis.config.exceptionConfig.exceptions;


import kiss.depot.redis.config.exceptionConfig.ExceptionReturn;
import kiss.depot.redis.model.enums.CommonErr;
import kiss.depot.redis.model.vo.response.Response;

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
