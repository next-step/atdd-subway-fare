package nextstep.subway.path.application.dto;

import nextstep.subway.station.application.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;
    private Long duration;
    private Long fare;

    public PathResponse(List<StationResponse> stations,
                        Long distance,
                        Long duration,
                        Long fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }

    public Long getFare() {
        return fare;
    }
}
