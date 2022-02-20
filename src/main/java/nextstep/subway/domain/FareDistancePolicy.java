package nextstep.subway.domain;

import nextstep.exception.fare.FareStrategyException;

import java.util.Objects;

public class FareDistancePolicy implements FarePolicyChain {

    private FarePolicyChain nextChain;

    @Override
    public void nextChain(FarePolicyChain chain) {
        this.nextChain = chain;
    }

    @Override
    public Fare calculate(Path path) {
        validate();
        FareCalculateStrategy strategy = new FareDistanceStrategy();
        Fare fare = strategy.calculate(path);

        return fare.add(nextChain.calculate(path));
    }

    private void validate() {
        if (Objects.isNull(nextChain)) {
            throw new FareStrategyException();
        }
    }

}
