package subway.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.constant.ErrorType;
import subway.common.exception.SubWayException;
import subway.line.domain.Line;
import subway.line.domain.model.LineRequest;
import subway.section.model.SectionRequest;
import subway.station.domain.StationService;
import subway.station.domain.model.StationResponse;

import java.util.List;

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
        sectionRepository.save(SectionRequest.of(line, req, stationService.findStation(req.getUpStationId()), stationService.findStation(req.getDownStationId())));
        return stationService.searchStationsInLine(req.getUpStationId(), req.getDownStationId());
    }

    @Transactional
    public List<StationResponse> addSectionToLine(Line line, SectionRequest req) {
        Section lastSection = lastSection(line);
        if (lastSection == null) {
            sectionRepository.save(SectionRequest.of(line, req, stationService.findStation(req.getUpStationId()), stationService.findStation(req.getDownStationId())));
            return stationService.searchStationsInLine(req.getUpStationId(), req.getDownStationId());
        }

        validSection(req, lastSection);
        sectionRepository.save(SectionRequest.of(line, req, lastSection.begin(), stationService.findStation(req.getDownStationId())));

        updateSubwayInfo(line, req, lastSection);

        return stationService.searchStationsInLine(lastSection.beginId(), req.getDownStationId());
    }

    private static void validSection(SectionRequest req, Section lastSection) {
        if (lastSection.endId() != req.getUpStationId()) {
            throw new SubWayException(ErrorType.UNABLE_TO_EXPAND);
        }
    }

    private static void updateSubwayInfo(Line line,
                                         SectionRequest req,
                                         Section lastSection) {
        lastSection.deactivate();
        line.update(lastSection.getDistance() + req.getDistance());
    }

    @Transactional
    public void removeSection(long id, long stationId) {
        List<Section> sections = sectionRepository.findByLineId(id);
        sections.forEach(it -> {
            if (it.beginId() == it.endId()) {
                throw new SubWayException(UNIQUE_SECTION);
            }

            int cnt = 0;
            if (it.endId() == stationId) {
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

    private Section lastSection(Line line) {
        if (line.emptySection()) {
            return null;
        }

        return line.getSections().lastSection();
    }
}
