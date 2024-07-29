package subway.station.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.model.Direction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @NoArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column
    private Boolean intersection;

    @Column
    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Builder
    public Station(String name) {
        this.name = name;
        this.intersection = false;
        this.direction = Direction.NONE;
    }
}
