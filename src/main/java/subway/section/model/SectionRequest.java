package subway.section.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.line.domain.Line;
import subway.line.domain.model.LineRequest;
import subway.section.Section;

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

    public static Section of(Line line, LineRequest req) {
        return Section.builder()
                      .begin(req.getUpStationId())
                      .end(req.getDownStationId())
                      .distance(req.getDistance())
                      .active(true)
                      .line(line)
                      .build();
    }

    public static Section of(Line line, SectionRequest request) {
        return Section.builder()
                      .begin(request.getUpStationId())
                      .end(request.getDownStationId())
                      .distance(request.getDistance())
                      .active(true)
                      .line(line)
                      .build();
    }
}
