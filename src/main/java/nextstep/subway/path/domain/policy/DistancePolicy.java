package nextstep.subway.path.domain.policy;

public class DistancePolicy {
    private static final int TEN_DISTANCE = 10;
    private static final int FIFTY_DISTANCE = 50;
    private static final int FIFTH_OVER_FARE = 5;
    private static final int EIGHT_OVER_FARE = 8;
    private static final int OVER_FARE = 100;
    private static final int ONE = 1;


    public static FarePolicy getDistancePolicy(int distance) {
        if (distance > TEN_DISTANCE && distance <= FIFTY_DISTANCE) {
            return new OverTenDistancePolicy(distance);
        }

        if (distance > FIFTY_DISTANCE) {
            return new OverFiftyDistancePolicy(distance);
        }
        return new DefaultDistancePolicy();
    }

    private static int calculate(int distance, int km) {
        return (int) ((Math.ceil((distance - ONE) / km) + ONE) * OVER_FARE);
    }

    public static class DefaultDistancePolicy implements FarePolicy {
        @Override
        public int fareCalculate(int fare) {
            return fare;
        }
    }

    public static class OverTenDistancePolicy implements FarePolicy {

        private final int distance;

        public OverTenDistancePolicy(int distance) {
            this.distance = distance;
        }

        @Override
        public int fareCalculate(int fare) {
            return fare + calculate(distance - TEN_DISTANCE, FIFTH_OVER_FARE);
        }

        private int calculate(int distance, int km) {
            return DistancePolicy.calculate(distance, km);
        }
    }

    public static class OverFiftyDistancePolicy implements FarePolicy {

        private final int distance;

        public OverFiftyDistancePolicy(int distance) {
            this.distance = distance;
        }

        @Override
        public int fareCalculate(int fare) {
            int overDistance = distance - FIFTY_DISTANCE;
            int overDistanceSubtract = distance - overDistance - TEN_DISTANCE;
            return fare + calculate(overDistanceSubtract, FIFTH_OVER_FARE) + calculate(overDistance, EIGHT_OVER_FARE);
        }

        private int calculate(int distance, int km) {
            return DistancePolicy.calculate(distance, km);
        }
    }
}
