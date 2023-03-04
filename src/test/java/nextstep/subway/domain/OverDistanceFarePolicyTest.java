package nextstep.subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class OverDistanceFarePolicyTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void 총_거리가_10KM_이하인_경우_추가_요금은_없다(int distance) {
        // when
        Fare fare = OverDistanceFarePolicy.create().calculateOverDistanceFare(distance);

        // then
        assertThat(fare).isEqualTo(Fare.of(0));
    }

    @ParameterizedTest
    @CsvSource({
            "11, 100",
            "15, 100",
            "16, 200",
            "45, 700",
            "46, 800",
            "50, 800"
    })
    void 총_거리에서_10KM_초과이고_50KM_이하인_거리는_5KM마다_추가_요금_100원이_추가된다(int distance, int expectedFare) {
        // when
        Fare fare = OverDistanceFarePolicy.create().calculateOverDistanceFare(distance);

        // then
        assertThat(fare).isEqualTo(Fare.of(expectedFare));
    }

    @ParameterizedTest
    @CsvSource({
            "50, 800",
            "51, 900",
            "58, 900",
            "59, 1000"
    })
    void 총_거리에서_50KM를_초과한_거리는_8KM마다_추가_요금_100원이_추가된다(int distance, int expectedFare) {
        // when
        Fare fare = OverDistanceFarePolicy.create().calculateOverDistanceFare(distance);

        // then
        assertThat(fare).isEqualTo(Fare.of(expectedFare));
    }
}
