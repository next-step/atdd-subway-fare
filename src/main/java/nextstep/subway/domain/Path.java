package nextstep.subway.domain;

import java.util.List;

public class Path {

    private static final int BASIC_FARE = 1250;
    private static final int OVER_FARE = 100;
    
    private static final int LEVEL1_OVER_FARE_INTERVAL = 5;
    private static final int LEVEL2_OVER_FARE_INTERVAL = 8;
    private static final int LEVEL1_OVER_FARE_POINT = 10;
    private static final int LEVEL2_OVER_FARE_POINT = 50;

    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getFare() {
        int distance = extractDistance();
        if (distance <= LEVEL1_OVER_FARE_POINT) {
            return BASIC_FARE;
        }
        if (distance <= LEVEL2_OVER_FARE_POINT) {
            return BASIC_FARE
                + calculateOverFare(distance - LEVEL1_OVER_FARE_POINT, LEVEL1_OVER_FARE_INTERVAL);
        }
        return BASIC_FARE
            + calculateOverFare(LEVEL2_OVER_FARE_POINT - LEVEL1_OVER_FARE_POINT, LEVEL1_OVER_FARE_INTERVAL)
            + calculateOverFare(distance - LEVEL2_OVER_FARE_POINT , LEVEL2_OVER_FARE_INTERVAL);
    }

    private int calculateOverFare(int distance, int interval) {
        return (int) ((Math.ceil((distance - 1) / interval) + 1) * OVER_FARE);
    }
}
