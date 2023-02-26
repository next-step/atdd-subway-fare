package nextstep.subway.domain;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class Path {

    private final List<Station> stations;

    private final int totalDistance;

    private final int totalDuration;

    private Path(List<Station> stations, int totalDistance, int totalDuration) {
        this.stations = Collections.unmodifiableList(stations);
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
    }

    public static Path of(Sections sections) {
        return new Path(sections.getStations(), sections.totalDistance(), sections.totalDuration());
    }

    public int getFare() {
        if (totalDistance <= 10) {
            return 1250;
        }

        if (totalDistance <= 50) {
            return 1250 + calculateOverFare(totalDistance - 10, 5);
        }

        return 1250 + calculateOverFare(totalDistance - 10, 8);
    }

    private int calculateOverFare(int distance, int kilometer) {
        return (int) ((Math.ceil((distance - 1) / kilometer) + 1) * 100);
    }
}
