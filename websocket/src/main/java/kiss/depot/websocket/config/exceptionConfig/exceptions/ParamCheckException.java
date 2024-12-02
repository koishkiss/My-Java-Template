package kiss.depot.websocket.config.exceptionConfig.exceptions;

import kiss.depot.websocket.model.enums.CommonErr;
import lombok.Getter;

@Getter
public class ParamCheckException extends CommonErrException {
    public ParamCheckException() {
        super(CommonErr.PARAM_WRONG);
    }

    public ParamCheckException(String msg) {
        super(CommonErr.PARAM_WRONG);
        ERROR.setMsg(msg);
    }

    @Override
    public String getMessage() {
        return "An exceptions occurred in param from front-end: " + super.getMessage();
    }
}
