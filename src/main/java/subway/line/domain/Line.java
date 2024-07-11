package subway.line.domain;

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
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "upstationid")
    private long upStationId;

    @Column(name = "downstationid")
    private long downStationId;

    @Column(name = "distance")
    private int distance;

    @Builder
    public Line(String name,
                String color,
                long upStationId,
                long downStationId,
                int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
