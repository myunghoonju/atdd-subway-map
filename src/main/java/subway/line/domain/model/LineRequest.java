package subway.line.domain.model;

import lombok.Getter;
import lombok.Setter;
import subway.line.domain.Line;

@Getter @Setter
public class LineRequest {

    private String name;

    private String color;

    private int upStationId;

    private int downStationId;

    private int distance;

    public static Line toEntity(LineRequest lineRequest) {
        return Line.builder()
                   .name(lineRequest.getName())
                   .color(lineRequest.getColor())
                   .upStationId(lineRequest.getUpStationId())
                   .downStationId(lineRequest.getDownStationId())
                   .distance(lineRequest.getDistance())
                   .build();
    }
}
