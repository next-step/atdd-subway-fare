package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

import java.util.Objects;

public abstract class AbstractFareCalculatorChain implements FareCalculatorChain {

    private FareCalculatorChain nextChain;

    @Override
    public int calculate(Path path, int initialFare) {
        if (!support(path)) {
            return callNextChain(path, initialFare);
        }
        var fare = convert(path, initialFare);
        return callNextChain(path, fare);
    }

    @Override
    public void setNextChain(FareCalculatorChain chain) {
        this.nextChain = chain;
    }

    private int callNextChain(Path path, int initialFare) {
        if (Objects.isNull(nextChain)) {
            return initialFare;
        }
        return nextChain.calculate(path, initialFare);
    }

    protected abstract int convert(Path path, int initialFare);
}
