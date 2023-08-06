package nextstep.subway.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateFareUtilsTest {

    private static final int DEFAULT_FARE = 1250;

    @Test
    @DisplayName("0km 면 요금이 없음")
    void whenZeroKm() {

        assertThat(CalculateFareUtils.getFare(0)).isZero();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    @DisplayName("10km 까지는 기본요금")
    void defaultFare(int distance) {

        assertThat(CalculateFareUtils.getFare(distance)).isEqualTo(DEFAULT_FARE);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 14, 15})
    @DisplayName("10km 부터는 5km 당 100원 (1번 부과)")
    void calFareUnder50_1(int distance) {

        assertThat(CalculateFareUtils.getFare(distance)).isEqualTo(DEFAULT_FARE + 100);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 19})
    @DisplayName("10km 부터는 5km 당 100원 (2번 부과)")
    void calFareUnder50_2(int distance) {

        assertThat(CalculateFareUtils.getFare(distance)).isEqualTo(DEFAULT_FARE + 200);
    }

    @Test
    @DisplayName("50km 이면 8번 부과")
    void calFareWhen50km() {
        assertThat(CalculateFareUtils.getFare(50)).isEqualTo(DEFAULT_FARE + 800);
    }

    @ParameterizedTest
    @ValueSource(ints = {51,58})
    @DisplayName("50km 초과시 8km 마다 100원 (1번 부과)")
    void calFareWhen50km(int distance) {
        assertThat(CalculateFareUtils.getFare(distance)).isEqualTo(DEFAULT_FARE + 800 + 100);
    }
}