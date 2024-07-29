package subway.line.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import subway.line.domain.Line;
import subway.station.domain.model.StationResponse;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineResponse {

    private long id;

    private String name;

    private String color;

    private List<LineStation> stations = new ArrayList<>();

    public static LineResponse of(Line line, List<StationResponse> stations) {
        return new LineResponse(line.getId(),
                                line.getName(),
                                line.getColor(),
                                LineStation.of(stations));
    }

    public static LineResponse of(LineResponse line, List<StationResponse> stations) {
        return new LineResponse(line.getId(),
                                line.getName(),
                                line.getColor(),
                                LineStation.of(stations));
    }
}
