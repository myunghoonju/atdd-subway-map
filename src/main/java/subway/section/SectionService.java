package subway.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.constant.ErrorType;
import subway.common.exception.SubWayException;
import subway.line.domain.Line;
import subway.line.domain.LineService;
import subway.line.domain.model.LineResponse;
import subway.section.model.SectionRequest;
import subway.station.domain.StationService;
import subway.station.domain.model.StationResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {

    private final LineService lineService;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    @Transactional
    public List<StationResponse> addSection(LineResponse res, SectionRequest sectionRequest) {
        Line line = editLine(res, sectionRequest);
        sectionRepository.save(SectionRequest.of(res.getId(), sectionRequest));

        return stationService.searchStationsInLine(line.getUpStationId(), line.getDownStationId());
    }

    private Line editLine(LineResponse line, SectionRequest sectionRequest) {
        return lineService.editLine(line.getId(), sectionRequest);
    }

    @Transactional
    public void removeSection(long id, long stationId) {
        LineResponse line = lineService.searchLine(id);
        if (line.getDownStationId() - line.getUpStationId() == 1) {
            throw new SubWayException(ErrorType.UNIQUE_SECTION);
        }

        if (line.getDownStationId() != stationId) {
            throw new SubWayException(ErrorType.NO_LAST_STATION);
        }

        Section section = sectionRepository.findByEndAndLineId(stationId, id);
        section.deactivate();
    }

    @Transactional
    public void rollbackLine(long id) {
        lineService.rollback(id, findLatestActiveSection(id));
    }

    private Section findLatestActiveSection(long id) {
        return sectionRepository.findTopByLineIdAndActiveOrderByEndDesc(id, true);
    }
}
