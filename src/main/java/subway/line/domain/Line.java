package subway.line.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import subway.common.exception.SubWayException;
import subway.section.Section;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static subway.common.constant.ErrorType.UNABLE_TO_EXPAND;

@Entity
@DynamicUpdate
@Getter @NoArgsConstructor
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    @Column(name = "distance")
    private int distance;

    @Builder
    public Line(String name, String color, int distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    //fixme
    public void rollback(long upStationId,
                         long downStationId,
                         int distance) {

        this.distance = distance;
    }

    public void updateStations(long upStationId,
                               long downStationId,
                               int distance) {
        validSection(upStationId, downStationId);

        this.distance = distance;
    }

    private void validSection(long upStationId, long downStationId) {
        if (!expandable(upStationId, downStationId)) {
            throw new SubWayException(UNABLE_TO_EXPAND);
        }
    }

    //fixme
    private boolean expandable(long upStationId, long downStationId) {
        return true;
    }
}
