package nextstep.subway.domain;

import java.math.BigDecimal;

public interface FareCalculator {

    BigDecimal calculate(int distance);

}
