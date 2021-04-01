package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.age.AgeFarePolicy;
import nextstep.subway.path.domain.policy.line.LineFarePolicy;

public class Fare extends FarePolicyTemplate {

    public static final int DEFAULT_FARE = 1250;
    public static final int INCREASE_FARE = 100;
    public static final int NUMBER_ONE = 1;

    public Fare(LineFarePolicy lineFarePolicy, AgeFarePolicy ageFarePolicy, int distance) {
        super(lineFarePolicy, ageFarePolicy, distance);
    }

    public void applyLineFarePolicy(LineFarePolicy farePolicy) {
        this.fare = farePolicy.calculateLineFare(fare);
    }

    @Override
    protected void applyAgeFarePolicy(AgeFarePolicy ageFarePolicy) {
        this.fare = ageFarePolicy.calculateAgeFare(fare);
    }

    public void addExtraCharge(int extraCharge) {
        this.fare += extraCharge;
    }
}
