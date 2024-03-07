package nextstep.subway.path;

import nextstep.subway.station.Station;

import java.util.List;

public class NewPathResponse {
    private List<Station> stations;
    private Long distance;
    private Long duration;

    public NewPathResponse(List<Station> stations, Long distance, Long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }
}
