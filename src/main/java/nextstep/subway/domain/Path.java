package nextstep.subway.domain;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static nextstep.subway.domain.DistanceFare.*;

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
        if (totalDistance <= DEFAULT_DISTANCE.getValue()) {
            return DEFAULT.getValue();
        }

        if (totalDistance <= DistanceFare.OVER_BETWEEN_TEN_AND_FIFTY.getValue()) {
            return DEFAULT.getValue() + calculateOverFare(
                    totalDistance - DEFAULT_DISTANCE.getValue(),
                    STANDARD_DISTANCE_OVER_BETWEEN_TEN_AND_FIFTY.getValue()
            );
        }

        return DEFAULT.getValue() + calculateOverFare(
                totalDistance - DEFAULT_DISTANCE.getValue(),
                STANDARD_FARE_DISTANCE_OVER_FIFTY.getValue()
        );
    }

    private int calculateOverFare(int distance, int kilometer) {
        return (int) ((Math.ceil(((distance - 1) / (double) kilometer)) + 1) * 100);
    }
}
