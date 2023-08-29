package nextstep.subway.path.domain.chainofresponsibility.fare;

import nextstep.subway.path.domain.Path;

public class DiscountFarePolicy extends AdditionalFarePolicy {
    public DiscountFarePolicy(FarePolicy next) {
        super(next);
    }

    @Override
    protected int afterCalculated(Path path, int fare) {
        return 0;
    }
}
