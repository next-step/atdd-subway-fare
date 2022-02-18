package nextstep.subway.domain;

public class FareLinePolicy implements FarePolicyChain {

    private FarePolicyChain nextChain;

    @Override
    public void nextChain(FarePolicyChain chain) {
        this.nextChain = chain;
    }

    @Override
    public Fare calculate(Path path) {
        FareCalculateStrategy strategy = new FareLineStrategy();
        return strategy.calculate(path);
    }

}
