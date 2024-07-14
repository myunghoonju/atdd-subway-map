package subway.section;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    @Column
    private long lineId;

    @Column
    private Boolean active;

    @Builder
    public Section(long begin,
                   long end,
                   int distance,
                   long lineId,
                   Boolean active) {
        this.begin = begin;
        this.end = end;
        this.distance = distance;
        this.lineId = lineId;
        this.active = active;
    }

    public void deactivate() {
        this.active = false;
    }
}
