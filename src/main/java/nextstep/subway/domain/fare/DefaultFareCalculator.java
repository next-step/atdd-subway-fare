package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

public class DefaultFareCalculator extends AbstractFareCalculatorChain {

    private static final int MINIMUM_FARE = 1250;

    @Override
    public boolean support(Path path) {
        return true;
    }

    @Override
    protected int convert(Path path, int initialFare) {
        return MINIMUM_FARE;
    }

}
