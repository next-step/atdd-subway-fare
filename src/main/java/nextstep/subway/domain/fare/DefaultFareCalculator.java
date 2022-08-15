package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

import java.util.Objects;

public class DefaultFareCalculator implements FareCalculatorChain {

    private static final int MINIMUM_FARE = 1250;

    private FareCalculatorChain nextChain;

    @Override
    public int calculate(Path path, int initialFare) {
        if (Objects.isNull(nextChain)) {
            return MINIMUM_FARE;
        }

        return nextChain.calculate(path, MINIMUM_FARE);
    }

    @Override
    public boolean support(Path path) {
        return true;
    }

    @Override
    public void setNextChain(FareCalculatorChain chain) {
        this.nextChain = chain;
    }

}
