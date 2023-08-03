package nextstep.subway.domain;

import nextstep.subway.domain.vo.Path;

public final class SubwayFare {

    private static final int BASIC_FARE = 1250;
    private static final int OVER_FARE = 100;
    private static final int OVER_FARE_PER_FIVE_KM = 5;
    private static final int OVER_FARE_PER_EIGHT_KM = 8;
    private static final int OVER_FARE_STANDARD_PER_FIVE_KM = 10;
    private static final int OVER_FARE_STANDARD_PER_EIGHT_KM = 50;


    private SubwayFare() {
    }

    public static int calculateFare(Path shortestPath) {
        int shortestPathDistance = (int) shortestPath.getDistance();
        if (shortestPathDistance < OVER_FARE_STANDARD_PER_FIVE_KM) {
            return BASIC_FARE;
        }

        if (shortestPathDistance < OVER_FARE_STANDARD_PER_EIGHT_KM) {
            return BASIC_FARE
                    + calculateOverFare(shortestPathDistance - OVER_FARE_STANDARD_PER_FIVE_KM, OVER_FARE_PER_FIVE_KM);
        }

        int 초과_요금 = calculateOverFare(shortestPathDistance - OVER_FARE_STANDARD_PER_EIGHT_KM, OVER_FARE_PER_EIGHT_KM);
        System.out.println(shortestPathDistance - OVER_FARE_STANDARD_PER_EIGHT_KM + "미터 초과_요금 = " + 초과_요금);
        return BASIC_FARE
                + calculateOverFare(OVER_FARE_STANDARD_PER_EIGHT_KM - OVER_FARE_STANDARD_PER_FIVE_KM, OVER_FARE_PER_FIVE_KM)
                + 초과_요금;
    }

    private static int calculateOverFare(int overDistance, int overFarePerKm) {
        return (int) ((Math.ceil((double) overDistance / overFarePerKm)) * OVER_FARE);
    }
}
