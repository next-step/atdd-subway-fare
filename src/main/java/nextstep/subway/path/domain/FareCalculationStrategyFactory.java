package nextstep.subway.path.domain;

public class FareCalculationStrategyFactory {

    public static final int DEFAULT_FARE_DISTANCE = 10;
    public static final int ADD_100_FARE_DISTANCE = 50;

    public static FareCalculationStrategy of(int distance) {
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return new DistanceUnder10FareStrategy(distance);
        }
        if (distance <= ADD_100_FARE_DISTANCE) {
            return new DistanceOver10Under50FareStrategy(distance);
        }
        return new DistanceOver50FareStrategy(distance);
    }



}
