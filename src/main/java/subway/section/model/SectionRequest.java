package subway.section.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.line.domain.Line;
import subway.line.domain.model.LineRequest;
import subway.section.Section;
import subway.station.domain.Station;

@Getter @Setter
@NoArgsConstructor
public class SectionRequest {

    private long upStationId;

    private long downStationId;

    private int distance;

    public SectionRequest(long upStationId,
                          long downStationId,
                          int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static Section of(Line line,
                             LineRequest req,
                             Station begin,
                             Station end) {
        return Section.builder()
                      .upStation(begin)
                      .downStation(end)
                      .distance(req.getDistance())
                      .active(true)
                      .line(line)
                      .build();
    }

    public static Section of(Line line,
                             SectionRequest req,
                             Station begin,
                             Station end) {
        return Section.builder()
                      .upStation(begin)
                      .downStation(end)
                      .distance(req.getDistance())
                      .active(true)
                      .line(line)
                      .build();
    }
}
