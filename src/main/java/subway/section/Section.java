package subway.section;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import subway.common.constant.ErrorType;
import subway.common.exception.SubWayException;
import subway.line.domain.Line;
import subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DynamicUpdate
@Getter @NoArgsConstructor
public class Section {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "begin",
                foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end",
                foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station downStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id",
                foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Line line;

    @Column
    private Boolean active;

    @Builder
    public Section(int distance,
                   Station upStation,
                   Station downStation,
                   Line line,
                   Boolean active) {
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
        this.active = active;
    }

    public void deactivate() {
        this.active = false;
    }

    public long beginId() {
        return upStation.getId();
    }

    public long endId() {
        return downStation.getId();
    }

    public Station begin() {
        if (upStation == null) {
            throw new SubWayException(ErrorType.NO_SUCH_STATION);
        }

        return upStation;
    }
}
