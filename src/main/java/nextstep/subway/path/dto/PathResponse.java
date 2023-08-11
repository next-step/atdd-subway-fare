package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fee;

    public static PathResponse of(List<StationResponse> stationResponses, Path path) {
        return new PathResponse(stationResponses, path.getTotalDistance(), path.getTotalDuration(), path.getFee());
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fee) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fee = fee;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public int getFee() {
        return fee;
    }
}
