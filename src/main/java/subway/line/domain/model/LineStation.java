package subway.line.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.station.domain.model.StationResponse;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class LineStation {

    private Long id;

    private String name;

    public LineStation(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<LineStation> of(List<StationResponse> stations) {
        List<LineStation> res = new ArrayList<>();
        if (stations.isEmpty()) {
            return res;
        }

        stations.forEach(station -> res.add(new LineStation(station.getId(), station.getName())));

        return res;
    }
}
