package nextstep.subway.domain;

public interface FarePolicyChain {

    void nextChain(FarePolicyChain calculator);

    Fare calculate(Path path);

}
