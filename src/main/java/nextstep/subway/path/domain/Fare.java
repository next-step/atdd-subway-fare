package nextstep.subway.path.domain;

public class Fare {

    public static final int DEFAULT_FARE = 1250;
    public static final int INCREASE_FARE = 100;
    public static final int NUMBER_ONE = 1;

    private final int fare;

    public Fare(FareRuleStrategy fareRuleStrategy, int distance) {
        this.fare = fareRuleStrategy.calculateFare(distance);
    }

    public int getFare() {
        return fare;
    }
}
