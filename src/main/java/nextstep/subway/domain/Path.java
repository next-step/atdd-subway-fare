package nextstep.subway.domain;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class Path {

    private static final int DEFAULT_FARE = 1250;

    private static final int DEFAULT_FARE_DISTANCE = 10;

    private static final int OVER_FARE_BETWEEN_TEN_AND_FIFTY = 50;

    private static final int STANDARD_FARE_DISTANCE_OVER_BETWEEN_TEN_AND_FIFTY = 5;

    private static final int STANDARD_FARE_DISTANCE_OVER_FIFTY = 8;

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
        if (totalDistance <= DEFAULT_FARE_DISTANCE) {
            return DEFAULT_FARE;
        }

        if (totalDistance <= OVER_FARE_BETWEEN_TEN_AND_FIFTY) {
            return DEFAULT_FARE + calculateOverFare(
                    totalDistance - DEFAULT_FARE_DISTANCE,
                    STANDARD_FARE_DISTANCE_OVER_BETWEEN_TEN_AND_FIFTY
            );
        }

        return DEFAULT_FARE + calculateOverFare(
                totalDistance - DEFAULT_FARE_DISTANCE,
                STANDARD_FARE_DISTANCE_OVER_FIFTY
        );
    }

    private int calculateOverFare(int distance, int kilometer) {
        return (int) ((Math.ceil(((distance - 1) / (double) kilometer)) + 1) * 100);
    }
}
