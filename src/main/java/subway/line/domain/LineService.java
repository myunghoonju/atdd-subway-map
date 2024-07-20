package subway.line.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.constant.ErrorType;
import subway.common.exception.SubWayException;
import subway.line.domain.model.LineRequest;
import subway.line.domain.model.LineResponse;
import subway.line.domain.model.LineUpdateRequest;
import subway.section.SectionService;
import subway.station.domain.StationService;
import subway.station.domain.model.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository repository;
    private final StationService stationService;
    private final SectionService sectionService;

    @Transactional(readOnly = true)
    public List<LineResponse> lines() {
        return repository.findAll()
                         .stream()
                         .map(it -> {
                                       List<StationResponse> stations = stationService.searchStationsInLine(it.getSections());
                                       return LineResponse.of(it, stations);
                         }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse line(long id) {
        Line line = repository.findLineById(id);
        if (line == null) {
            throw new SubWayException(ErrorType.NO_SUCH_LINE);
        }

        List<StationResponse> stations = stationService.searchStationsInLine(line.getSections());

        return LineResponse.of(line, stations);
    }

    @Transactional(readOnly = true)
    public Line searchLine(long id) {
        Line line = repository.findLineById(id);
        if (line == null) {
            throw new SubWayException(ErrorType.NO_SUCH_LINE);
        }

        return line;
    }

    @Transactional
    public LineResponse addLine(LineRequest lineReq) {
        Line saved = repository.save(LineRequest.toEntity(lineReq));
        return LineResponse.of(saved, sectionService.addSection(saved, lineReq));
    }

    @Transactional
    public void editLine(long id, LineUpdateRequest lineReq) {
        Line line = repository.findLineById(id);
        line.update(lineReq.getName(), lineReq.getColor());
    }

    @Transactional
    public void removeLine(long id) {
        repository.deleteById(id);
    }
}
