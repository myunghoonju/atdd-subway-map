package subway.station.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.constant.ErrorType;
import subway.common.exception.SubWayException;
import subway.station.domain.model.StationRequest;
import subway.station.domain.model.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public List<StationResponse> searchStationsInLine(long up, long down) {
        List<StationResponse> res = new ArrayList<>();
        for (long stationId = up; stationId <= down; stationId++) {
            res.add(createStationResponse(stationRepository.findById(stationId)));
        }

        return res;
    }

    private StationResponse createStationResponse(Station station) {
        if (station == null) {
            throw new SubWayException(ErrorType.NO_SUCH_STATION);
        }

        return new StationResponse(station.getId(), station.getName());
    }
}
