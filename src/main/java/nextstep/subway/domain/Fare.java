package nextstep.subway.domain;

import nextstep.subway.domain.policy.AdditionalFarePolicy;
import nextstep.subway.domain.policy.DiscountFarePolicy;
import nextstep.subway.domain.policy.DiscountType;
import nextstep.subway.domain.policy.DistanceFarePolicy;
import nextstep.subway.domain.policy.FarePolicy;

public class Fare {
    private FarePolicy farePolicy;

    public Fare(int distance, int additionalFare, int age) {
        this.farePolicy = new DiscountFarePolicy(
                age,
                new AdditionalFarePolicy(
                        additionalFare,
                        new DistanceFarePolicy(distance)
                )
        );
    }

    public Fare(int distance, int additionalFare, DiscountType discountType) {
        this.farePolicy = new DiscountFarePolicy(
                discountType,
                new AdditionalFarePolicy(
                        additionalFare,
                        new DistanceFarePolicy(distance)
                )
        );
    }

    public Fare(int distance) {
        this(distance, 0, DiscountType.ADULT);
    }

    public int value() {
        return farePolicy.calculate();
    }
}
