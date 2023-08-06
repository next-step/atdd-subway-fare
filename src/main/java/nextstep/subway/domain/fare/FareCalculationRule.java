package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

public interface FareCalculationRule {
    int calculateFare(Path path, int age, int totalFare);

    void setNextRule(FareCalculationRule nextRule);
}
