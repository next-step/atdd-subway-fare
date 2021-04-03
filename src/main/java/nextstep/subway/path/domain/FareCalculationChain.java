package nextstep.subway.path.domain;

public interface FareCalculationChain {

    int DEFAULT_FARE = 1250;

    void setNextChain(FareCalculationChain nextChain);
    Fare calculate(FareCalculationCriteria criteria, Fare fare);

}
