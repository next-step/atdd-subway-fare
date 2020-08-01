package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.fare.domain.FareContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareCalculatorTest {

    @DisplayName("기본 요금을 계산한다.")
    @Test
    void calculate() {
        // given
        FareCalculator fareCalculator = new FareCalculator();

        // when
        int distance = 0;
        FareContext fareContext = new FareContext(distance);
        Fare fare = fareCalculator.calculate(fareContext);

        // then
        Assertions.assertThat(fare.getValue()).isEqualTo(1250);
    }
}