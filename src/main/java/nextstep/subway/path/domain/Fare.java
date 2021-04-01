package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.age.AgeFarePolicy;
import nextstep.subway.path.domain.policy.line.LineFarePolicy;

public class Fare {

    public static final int DEFAULT_FARE = 1250;
    public static final int INCREASE_FARE = 100;
    public static final int NUMBER_ONE = 1;

    private int fare;

    public Fare(int distance) {
        this.fare = distance;
    }

    public void applyLineFarePolicy(LineFarePolicy farePolicy) {
        this.fare = farePolicy.calculateLineFare(this.fare);
    }

    public void applyAgeFarePolicy(AgeFarePolicy policy) {
        this.fare = policy.calculateAgeFare();
    }

    public void addExtraCharge(int extraCharge) {
        this.fare += extraCharge;
    }

    public int getFare() {
        return fare;
    }
}
