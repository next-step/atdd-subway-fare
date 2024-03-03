package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.domain.fareOption.Fare10KmTo50KmOption;
import nextstep.subway.domain.fareOption.Fare50KmOverOption;
import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.FareCalculatorImpl;
import org.junit.jupiter.api.Test;

public class FareDistanceTest {
    private static FareCalculator fareCalculator = new FareCalculatorImpl(
        List.of(
            new Fare10KmTo50KmOption(),
            new Fare50KmOverOption()
        )
    );


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
