package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class FareResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    public FareResponse() {
    }

    public FareResponse(List<StationResponse> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public static FareResponse of(PathResult pathResult, int fare) {
        int distance = pathResult.getTotalDistance();
        int duration = pathResult.getTotalDuration();
        return new FareResponse(StationResponse.listOf(pathResult.getStations()), distance, duration, fare);
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

    public int getFare() {
        return fare;
    }
}
