package subway.line.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LineRequest {

    private String name;

    private String color;

    private int upStationId;

    private int downStationId;

    private int distance;
}
