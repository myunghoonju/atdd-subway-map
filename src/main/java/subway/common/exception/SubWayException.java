package subway.common.exception;

import subway.common.constant.ErrorType;

public class SubWayException extends RuntimeException {

    public SubWayException(ErrorType type) {
        super(type.getMsg());
    }
}
