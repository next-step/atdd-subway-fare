package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.FarePolicy;

public class NoDiscount implements FarePolicy {

    @Override
    public int calculateFare(int fare) {
        return fare;
    }
}
