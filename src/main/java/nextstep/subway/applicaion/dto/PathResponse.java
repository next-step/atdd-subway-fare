package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Path;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private BigDecimal fare;

    public PathResponse(final List<StationResponse> stations, final int distance, final int duration, final BigDecimal fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public static PathResponse of(final Path path, final Fare fare) {
        final List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return new PathResponse(stations, path.getShortestDistance(), path.getShortestDuration(), fare.getFare());
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

    public BigDecimal getFare() {
        return fare;
    }
}
