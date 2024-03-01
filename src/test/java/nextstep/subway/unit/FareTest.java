package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.FareCalculatorImpl;
import org.junit.jupiter.api.Test;

public class FareTest {
    private static FareCalculator fareCalculator = new FareCalculatorImpl();


    @Test
    void 요금_계산__10km이내_기본요금() {
        int distance = 10;
        int fare = fareCalculator.calculateFare(distance);

        assertThat(fare).isEqualTo(1_250);
    }

    @Test
    void 요금_계산__10km초과_50km_이내_5km마다_100원_추가_요금() {
        int distance = 50;
        int fare = fareCalculator.calculateFare(distance);

        assertThat(fare).isEqualTo(2_050);
    }

    @Test
    void 요금_계산__50km초과_8km마다_100원_추가_요금() {
        int distance = 100;
        int fare = fareCalculator.calculateFare(distance);

        assertThat(fare).isEqualTo(2_750);
    }
}
