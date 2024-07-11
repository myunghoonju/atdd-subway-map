package subway.line.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.model.LineRequest;
import subway.line.domain.model.LineResponse;
import subway.line.domain.model.LineUpdateRequest;
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
                                       List<StationResponse> stations = stationService.searchStationsInLine(it.getDownStationId(),
                                                                                                            it.getUpStationId());
                                       return LineResponse.of(it, stations);
                         }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse searchLine(long id) {
        Line line = repository.findLineById(id);
        if (line == null) {
            return null;
        }

        List<StationResponse> stations = stationService.searchStationsInLine(line.getDownStationId(), line.getUpStationId());

        return LineResponse.of(line, stations);
    }

    @Transactional
    public LineResponse addLine(LineRequest lineReq) {
        Line saved = repository.save(LineRequest.toEntity(lineReq));
        List<StationResponse> stations = stationService.searchStationsInLine(lineReq.getDownStationId(), lineReq.getUpStationId());

        return LineResponse.of(saved, stations);
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
