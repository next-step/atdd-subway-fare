package nextstep.subway.domain.path;

import nextstep.subway.domain.path.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceRangeTest {

    @DisplayName("거리가 10km 미만이면 요금이 1250원이 계산된다.")
    @Test
    void fee_distance_10km_under() {
        final Fare fare = DistanceRange.UP_TO_10.calculate(new Distance(10));

        assertThat(fare).isEqualTo(new Fare(1250));
    }

    @DisplayName("거리가 10km 초과면 5km마다 100원의 추가요금이 청구된다.")
    @ParameterizedTest
    @CsvSource({ "11, 1350", "16, 1450"})
    void fee_distance_10km_over(int distance, int expectedFare) {
        final Fare fare = DistanceRange.ABOVE_10_UP_TO_50.calculate(new Distance(distance));

        assertThat(fare).isEqualTo(new Fare(expectedFare));
    }

    @DisplayName("거리가 50km 초과면 8km마다 100원의 추가요금이 청구된다.")
    @ParameterizedTest
    @CsvSource({ "51, 2150", "59, 2250"})
    void fee_distance_50km_over(int distance, int expectedFare) {
        final Fare fare = DistanceRange.ABOVE_50.calculate(new Distance(distance));

        assertThat(fare).isEqualTo(new Fare(expectedFare));
    }
}
