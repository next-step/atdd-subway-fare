package nextstep.path.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFareCalculatorTest {

    @Test
    @DisplayName("거리로 요금을 계산할 수 있다.")
    void calculateFareTest() {
        final PathFareCalculator pathFareCalculator = new PathFareCalculator();
        final long fare = pathFareCalculator.calculate(9);

        assertThat(fare).isEqualTo(1250L);
    }
}
