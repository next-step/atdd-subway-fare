package nextstep.subway.domain;

public class FiftiethLessDistanceCalculator implements FareCalculator {
    private static final int BASE_FARE_DISTANCE = 10;
    private static final int LONG_FARE_DISTANCE = 50;
    private static final int EXTRA_FARE_LESS_LONG_DISTANCE = 5;
    private FareCalculator nextCalculator;

    public void nextFareChain(FareCalculator nextCalculator){
        this.nextCalculator = nextCalculator;
    }

    @Override
    public int calculate(int totalFare, int totalDistance) {
        if (totalDistance > LONG_FARE_DISTANCE) {
            totalFare += (int) ((Math.ceil((LONG_FARE_DISTANCE - BASE_FARE_DISTANCE - 1) / EXTRA_FARE_LESS_LONG_DISTANCE) + 1) * 100);
        }
        if (totalDistance > BASE_FARE_DISTANCE && totalDistance <= LONG_FARE_DISTANCE) {
            totalFare += (int) ((Math.ceil((totalDistance - BASE_FARE_DISTANCE - 1) / EXTRA_FARE_LESS_LONG_DISTANCE) + 1) * 100);
        }
        if(this.nextCalculator == null){
            return totalFare;
        }
        return this.nextCalculator.calculate(totalFare, totalDistance);
    }

}
