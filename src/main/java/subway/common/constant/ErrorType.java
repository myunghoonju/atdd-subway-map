package subway.common.constant;

import lombok.Getter;

@Getter
public enum ErrorType {

    NO_SUCH_LINE("존재하지 않는 노선입니다"),
    NO_SUCH_STATION("존재하지 않는 역입니다"),
    UNABLE_TO_EXPAND("상행역이 아니거나 이미 노선에 존재하는 역입니다"),
    UNIQUE_SECTION("구간이 하나 입니다"),
    NO_LAST_STATION("마지막 역이 아닙니다")
    ;

    private String msg;

    ErrorType(String msg) {}
}
