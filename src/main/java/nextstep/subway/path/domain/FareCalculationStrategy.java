package nextstep.subway.path.domain;

public interface FareCalculationStrategy {

    public static final int DEFAULT_FARE_DISTANCE = 10;
    public static final int ADD_100_FARE_DISTANCE = 50;
    public static final int DEFAULT_FARE = 1250;

    int calculate(int distance);
}

