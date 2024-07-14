package subway.line.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.constant.ErrorType;
import subway.common.exception.SubWayException;
import subway.line.domain.model.LineRequest;
import subway.line.domain.model.LineResponse;
import subway.line.domain.model.LineUpdateRequest;
import subway.section.Section;
import subway.section.model.SectionRequest;
import subway.station.domain.StationService;
import subway.station.domain.model.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository repository;
    private final StationService stationService;

    @Transactional(readOnly = true)
    public List<LineResponse> lines() {
        return repository.findAll()
                         .stream()
                         .map(it -> {
                                       List<StationResponse> stations = stationService.searchStationsInLine(it.getUpStationId(),
                                                                                                            it.getDownStationId());
                                       return LineResponse.of(it, stations);
                         }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse searchLine(long id) {
        Line line = repository.findLineById(id);
        if (line == null) {
            throw new SubWayException(ErrorType.NO_SUCH_LINE);
        }

        List<StationResponse> stations = stationService.searchStationsInLine(line.getUpStationId(), line.getDownStationId());

        return LineResponse.of(line, stations);
    }

    @Transactional
    public LineResponse addLine(LineRequest lineReq) {
        Line saved = repository.save(LineRequest.toEntity(lineReq));
        List<StationResponse> stations = stationService.searchStationsInLine(lineReq.getUpStationId(), lineReq.getDownStationId());

        return LineResponse.of(saved, stations);
    }

    @Transactional
    public void editLine(long id, LineUpdateRequest lineReq) {
        Line line = repository.findLineById(id);
        line.update(lineReq.getName(), lineReq.getColor());
    }

    @Transactional
    public Line editLine(long id, SectionRequest section) {
        Line line = repository.findLineById(id);
        line.updateStations(section.getUpStationId(),
                            section.getDownStationId(),
                            section.getDistance());

        return line;
    }

    @Transactional
    public void rollback(long id, Section section) {
        Line line = repository.findLineById(id);
        line.rollback(section.getBegin(),
                      section.getEnd(),
                      section.getDistance());
    }

    @Transactional
    public void removeLine(long id) {
        repository.deleteById(id);
    }
}
