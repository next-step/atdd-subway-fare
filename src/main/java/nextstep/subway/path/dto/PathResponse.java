package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.fare.distance.DistanceFarePolicies;
import nextstep.subway.path.domain.fare.line.LineFarePolicy;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int duration;
    private final int fare;

    public static PathResponse of(Path path, DistanceFarePolicies distanceFarePolicies, LineFarePolicy lineFarePolicy) {
        List<StationResponse> stationResponses = path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, path.getTotalDistance(), path.getTotalDuration(), path.calculateFare(distanceFarePolicies, lineFarePolicy));
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
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
