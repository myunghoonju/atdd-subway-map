package subway.section.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public static Section of(long lineId, SectionRequest request) {
        return Section.builder()
                      .begin(request.upStationId)
                      .end(request.downStationId)
                      .distance(request.distance)
                      .lineId(lineId)
                      .active(true)
                      .build();
    }
}
