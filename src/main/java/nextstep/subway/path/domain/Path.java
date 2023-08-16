package nextstep.subway.path.domain;

import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private static final int BASIC_FEE = 1250;
    private static final int SHORT_DISTANCE_LIMIT = 10;
    private static final int MIDDLE_DISTANCE_LIMIT = 50;
    private static final int MIDDLE_DISTANCE_UNIT = 5;
    private static final int LONG_DISTANCE_UNIT = 8;
    private static final int OVER_FARE = 100;

    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getTotalDistance() {
        return sections.calculateTotalDistance();
    }

    public int getTotalDuration() {
        return sections.calculateTotalDuration();
    }

    /**
     * 1. 10 이하 : 기본요금
     * 2. 10 초과 ~ 50 이하 : 기본요금 + 100원/5km
     * 3. 50 초과 : 기본요금 + 100원/5km (40) + 100원/8km
     */
    public int calculateFare() {
        int totalDistance = getTotalDistance();

        if (totalDistance <= SHORT_DISTANCE_LIMIT) {
            return BASIC_FEE;
        }

        if (totalDistance <= MIDDLE_DISTANCE_LIMIT) {
            return calculateMiddleDistance(totalDistance);
        }

        return calculateLongDistance(totalDistance);

    }

    private int calculateMiddleDistance(int totalDistance) {
        int lastDistance = totalDistance - SHORT_DISTANCE_LIMIT;
        return BASIC_FEE + calculateOverFare(lastDistance, MIDDLE_DISTANCE_UNIT);
    }

    private int calculateLongDistance(int totalDistance) {
        int lastDistance = totalDistance - MIDDLE_DISTANCE_LIMIT;
        int middleDistance = totalDistance - lastDistance - SHORT_DISTANCE_LIMIT;

        return BASIC_FEE + calculateOverFare(middleDistance, MIDDLE_DISTANCE_UNIT) + calculateOverFare(lastDistance, LONG_DISTANCE_UNIT);
    }

    private int calculateOverFare(int distance, int distanceUnit) {
        return ((distance - 1) / distanceUnit + 1) * OVER_FARE;
    }
}
