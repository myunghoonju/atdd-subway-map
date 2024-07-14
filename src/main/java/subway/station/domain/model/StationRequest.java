package subway.station.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StationRequest {

    private String name;

    public StationRequest(String name) {
        this.name = name;
    }
}
