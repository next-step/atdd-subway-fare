package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.domain.Fare.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("10Km 이내 거리는 기본 요금을 받는다.")
    @ValueSource(ints = {1, 10})
    @ParameterizedTest(name = "거리: {0}Km")
    void defaultFare(int distance) {
        Fare fare = new Fare(distance);

        assertThat(fare.cost()).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("10Km 초과 시 추가운임이 부과된다.")
    @CsvSource({"12,1350", "16,1450"})
    @ParameterizedTest(name = "거리: {0}Km")
    void extra10KmFare(int distance, int expected) {
        Fare fare = new Fare(distance);

        assertThat(fare.cost()).isEqualTo(expected);
    }

    @DisplayName("50Km 초과 시 추가운임이 부과된다.")
    @CsvSource({"66,2250", "75,2450"})
    @ParameterizedTest(name = "거리: {0}Km")
    void extra50KmFare(int distance, int expected) {
        Fare fare = new Fare(distance);

        assertThat(fare.cost()).isEqualTo(expected);
    }
}
