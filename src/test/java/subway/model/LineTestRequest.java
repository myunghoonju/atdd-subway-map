package subway.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.line.domain.model.LineRequest;

@Getter @Setter
@NoArgsConstructor
public class LineTestRequest extends LineRequest {

    public LineTestRequest(String name,
                           String color,
                           int upStationId,
                           int downStationId,
                           int distance) {
        setName(name);
        setColor(color);
        setUpStationId(upStationId);
        setDownStationId(downStationId);
        setDistance(distance);
    }
}
