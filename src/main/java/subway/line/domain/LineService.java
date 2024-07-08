package subway.line.domain;

import org.springframework.stereotype.Service;
import subway.line.domain.model.LineRequest;
import subway.line.domain.model.LineResponse;
import subway.line.domain.model.LineUpdateRequest;
import subway.station.domain.StationService;
import subway.station.domain.model.StationResponse;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class LineService {

    private final LineRepository repository;
    private final StationService stationService;

    public LineService(LineRepository repository,
                       StationService stationService) {
        this.repository = repository;
        this.stationService = stationService;
    }

    public List<LineResponse> lines() {
        List<LineResponse> res = new ArrayList<>();
        repository.findAll().forEach(it -> {
            List<StationResponse> stations = stationService.stations(it.getDownStationId(), it.getUpStationId());
            res.add(LineResponse.of(it, stations));
        });

        return res;
    }

    public LineResponse line(long id) {
        Line line = repository.findLineById(id);
        if (line == null) {
            return null;
        }

        List<StationResponse> stations = stationService.stations(line.getDownStationId(), line.getUpStationId());

        return LineResponse.of(line, stations);
    }

    public LineResponse addLine(LineRequest lineReq) {
        Line saved = repository.save(LineRequest.toEntity(lineReq));
        List<StationResponse> stations = stationService.stations(lineReq.getDownStationId(), lineReq.getUpStationId());

        return LineResponse.of(saved, stations);
    }

    @Transactional
    public void editLine(long id, LineUpdateRequest lineReq) {
        Line line = repository.findLineById(id);
        line.update(lineReq.getName(), lineReq.getColor());
    }

    public void removeLine(long id) {
        repository.deleteById(id);
    }
}
