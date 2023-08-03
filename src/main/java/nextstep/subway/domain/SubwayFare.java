package nextstep.subway.domain;

import nextstep.subway.domain.vo.Path;

public final class SubwayFare {

    public static final int BASIC_FARE = 1250;
    private static final int OVER_FARE = 100;
    private static final int OVER_FARE_PER_KM = 5;
    private static final int OVER_FARE_STANDARD_PER_FIVE_KM = 10;
    private static final int OVER_FARE_STANDARD_PER_EIGHT_KM = 50;

    private SubwayFare() {
    }

    public static int calculateFare(Path shortestPath) {
        int shortestPathDistance = (int) shortestPath.getDistance();
        if (shortestPathDistance <= OVER_FARE_STANDARD_PER_FIVE_KM) {
            return BASIC_FARE;
        } else if (shortestPathDistance <= OVER_FARE_STANDARD_PER_EIGHT_KM) {
            return BASIC_FARE + calculateOverFare(shortestPathDistance - OVER_FARE_STANDARD_PER_FIVE_KM);
        }
        return BASIC_FARE + calculateOverFare(shortestPathDistance - OVER_FARE_STANDARD_PER_FIVE_KM);
    }

    private static int calculateOverFare(int overDistance) {
        return (int) ((Math.ceil((double) (overDistance - 1) / OVER_FARE_PER_KM) + 1) * OVER_FARE);
    }
}
