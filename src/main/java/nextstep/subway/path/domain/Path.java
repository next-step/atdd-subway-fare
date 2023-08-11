package nextstep.subway.path.domain;

import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private static final int BASIC_FEE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private static final int LAST_DISTANCE = 50;

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
        int result = BASIC_FEE;

        if (BASIC_DISTANCE < totalDistance && totalDistance <= LAST_DISTANCE) {
            int lastDistance = totalDistance - BASIC_DISTANCE;
            result += calculateOverFare(lastDistance, 5);
        }

        if (LAST_DISTANCE < totalDistance) {
            int lastDistance = totalDistance - LAST_DISTANCE;
            int middleDistance = totalDistance - lastDistance - BASIC_DISTANCE;

            result += calculateOverFare(middleDistance, 5) + calculateOverFare(lastDistance, 8);
        }

        return result;
    }

    private int calculateOverFare(int distance, int additionalFeeDistance) {
        return ((distance - 1) / additionalFeeDistance + 1) * 100;
    }
}
