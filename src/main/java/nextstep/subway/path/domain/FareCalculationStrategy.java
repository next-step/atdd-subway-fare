package nextstep.subway.path.domain;

public interface FareCalculationStrategy {

    int DEFAULT_FARE = 1250;

    int calculate();
}

