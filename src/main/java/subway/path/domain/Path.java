package subway.path.domain;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.Station;

import java.util.List;

@Getter
@Builder
public class Path {
    private long totalDistance;
    private long totalDuration;
    private long totalFare;
    private List<Station> stations;
}
