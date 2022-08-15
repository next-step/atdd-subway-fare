package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

public interface FareCalculatorChain {
    int calculate(Path path, int initialFare);
    boolean support(Path path);
    void setNextChain(FareCalculatorChain chain);
}
