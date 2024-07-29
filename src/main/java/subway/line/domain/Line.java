package subway.line.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import subway.section.Sections;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    @Embedded
    private Sections sections;

    @Column(name = "distance")
    private int distance;

    @Builder
    public Line(String name, String color, int distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public void update(int newDistance) {
        distance = newDistance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean emptySection() {
        return sections.list().isEmpty();
    }

    public void refresh() {
        distance = sections.lastSection().getDistance();
    }
}
