package subway.section;

import lombok.Getter;
import subway.common.constant.ErrorType;
import subway.common.exception.SubWayException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line")
    List<Section> sections = new ArrayList<>();

    public List<Section> list() {
        return sections.stream()
                       .filter(Section::getActive)
                       .collect(Collectors.toList());
    }

    public Section lastSection() {
        return sections.stream()
                       .filter(Section::getActive)
                       .sorted(Comparator.comparing(Section::getId))
                       .limit(1)
                       .findFirst()
                       .orElseThrow(() -> new SubWayException(ErrorType.NO_SUCH_STATION));
    }
}
