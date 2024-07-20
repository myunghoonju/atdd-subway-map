package subway.section;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import subway.line.domain.Line;
import subway.section.model.Direction;
import subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@Getter @NoArgsConstructor
public class Section {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private long begin;

    @Column
    private long end;

    @Column
    private int distance;

    @OneToMany(mappedBy = "id")
    private List<Station> stations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id",
                foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Line line;

    @Column
    private Boolean active;

    @Builder
    public Section(long begin,
                   long end,
                   int distance,
                   List<Station> stations,
                   Line line,
                   Boolean active) {
        this.begin = begin;
        this.end = end;
        this.distance = distance;
        this.stations = stations;
        this.line = line;
        this.active = active;
    }

    public void deactivate() {
        this.active = false;
    }
}
