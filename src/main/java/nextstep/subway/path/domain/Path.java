package nextstep.subway.path.domain;

import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private static final int BASIC_FEE = 1250;

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

        if (totalDistance <= 10) {
            return BASIC_FEE;
        }

        if (totalDistance <= 50) {
            int remain = totalDistance - 10;
            return BASIC_FEE + calculateOverFare(remain, 5);
        }

        int remain = totalDistance - 50;
        return BASIC_FEE + calculateOverFare(40, 5) + calculateOverFare(remain, 8);
    }

    private int calculateOverFare(int distance, int additionalFeeDistance) {
        return ((distance - 1) / additionalFeeDistance + 1) * 100;
    }
}
