package subway.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.constant.ErrorType;
import subway.common.exception.SubWayException;
import subway.line.domain.Line;
import subway.line.domain.model.LineRequest;
import subway.section.model.SectionRequest;
import subway.station.domain.StationService;
import subway.station.domain.model.StationResponse;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static subway.common.constant.ErrorType.NO_LAST_STATION;
import static subway.common.constant.ErrorType.UNIQUE_SECTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {

    private final StationService stationService;
    private final SectionRepository sectionRepository;

    @Transactional
    public List<StationResponse> addSection(Line line, LineRequest req) {
        sectionRepository.save(SectionRequest.of(line, req));
        return stationService.searchStationsInLine(req.getUpStationId(), req.getDownStationId());
    }

    @Transactional
    public List<StationResponse> addSectionToLine(Line line, SectionRequest req) {
        Section lastSection = lastSection(line);
        if (lastSection != null) {
            validSection(req, lastSection);
        }

        sectionRepository.save(SectionRequest.of(line, req));

        return stationService.searchStationsInLine(firstSection(line, req.getUpStationId()).getBegin(), req.getDownStationId());
    }

    private static void validSection(SectionRequest req, Section lastSection) {
        if (lastSection.getEnd() != req.getUpStationId()) {
            throw new SubWayException(ErrorType.UNABLE_TO_EXPAND);
        }
    }

    @Transactional
    public void removeSection(long id, long stationId) {
        List<Section> sections = sectionRepository.findByLineId(id);
        sections.forEach(it -> {
            if (it.getBegin() == it.getEnd()) {
                throw new SubWayException(UNIQUE_SECTION);
            }

            int cnt = 0;
            if (it.getEnd() == stationId) {
                cnt++;
                it.deactivate();
            }

            if (cnt != 1) {
                throw new SubWayException(NO_LAST_STATION);
            }
        });
    }

    @Transactional
    public void deactivateSection(long id) {
        List<Section> sections = sectionRepository.findByLineId(id);
        sections.forEach(Section::deactivate);
    }

    private Section firstSection(Line line, long reqUpStationId) {
        if (line.getSections().isEmpty()) {
            return Section.builder().begin(reqUpStationId).build();
        }

        return line.getSections().stream()
                                 .filter(Section::getActive)
                                 .sorted(Comparator.comparing(Section::getId).reversed())
                                 .limit(1).collect(Collectors.toList())
                                 .get(0);
    }

    private Section lastSection(Line line) {
        if (line.getSections().isEmpty()) {
            return null;
        }

        return line.getSections().stream()
                                 .filter(Section::getActive)
                                 .sorted(Comparator.comparing(Section::getId))
                                 .limit(1).collect(Collectors.toList())
                                 .get(0);
    }
}
