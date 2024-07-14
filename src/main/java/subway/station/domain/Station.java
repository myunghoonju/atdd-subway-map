package subway.station.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.domain.Line;

import javax.persistence.*;

@Entity
@Getter @NoArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column
    private Boolean intersection; // 환승가능여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    public Station(String name) {
        this.name = name;
    }
}
