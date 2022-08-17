package nextstep.subway.domain;

public class Fare {

    private static final int BASE_FARE = 1250;

    private FarePolicy policy;

    public Fare(FarePolicy policy) {
        this.policy = policy;
    }

    public int calculate() {
        return policy.proceed(BASE_FARE);
    }

}
