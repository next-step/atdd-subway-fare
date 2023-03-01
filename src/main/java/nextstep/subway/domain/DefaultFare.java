package nextstep.subway.domain;

public class DefaultFare implements Fare {

    private static final int DEFAULT_FARE = 1_250;

    @Override
    public int calculateOverFare(int distance) {

        return DEFAULT_FARE;
    }
}
