package nextstep.subway.domain;

public class DistanceFarePolicy extends FarePolicy {

    private static final int KM_FREE_FARE = 10;

    private static final int KM_BASED_ON_100_WON_FARE = 50;

    private static final int EXTRA_FARE_BASIC_UNIT = 100;


    private final int distance;

    public DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean applicable() {
        return distance > KM_FREE_FARE;
    }

    @Override
    public int calculate(int fare) {
        if (distance <= KM_BASED_ON_100_WON_FARE) {
            return fare + calculateOverFare(KM_FREE_FARE, distance, 5);
        }

        int extraFareUnder50Km = calculateOverFare(KM_FREE_FARE, KM_BASED_ON_100_WON_FARE, 5);
        int extraFareOver50Km = calculateOverFare(KM_BASED_ON_100_WON_FARE, distance, 8);

        return fare + extraFareUnder50Km + extraFareOver50Km;
    }


    private int calculateOverFare(int from, int to, int divisor) {
        return (int) ((Math.ceil((to - from - 1) / divisor) + 1) * EXTRA_FARE_BASIC_UNIT);
    }

}
