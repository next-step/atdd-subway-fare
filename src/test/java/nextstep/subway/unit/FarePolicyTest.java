package nextstep.subway.unit;

import nextstep.subway.domain.FarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.FarePolicy.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 정책 관련 테스트")
class FarePolicyTest {

    @DisplayName("10KM 요금 계산")
    @Test
    void defaultFare() {
        FarePolicy farePolicy = FarePolicy.of(9);
        assertThat(farePolicy.calculate(9)).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("10km 초과 요금 조회")
    @Test
    void over10KmFare() {
        FarePolicy farePolicy = FarePolicy.of(10);
        assertThat(farePolicy.calculate(10)).isEqualTo(DEFAULT_FARE + 100);
    }

    @DisplayName("49km 요금 조회")
    @Test
    void over49KmFare() {
        FarePolicy farePolicy = FarePolicy.of(49);
        assertThat(farePolicy.calculate(49)).isEqualTo(DEFAULT_FARE + 800);
    }

    @DisplayName("50km 초과 요금 조회")
    @Test
    void over50KmFare() {
        FarePolicy farePolicy = FarePolicy.of(50);
        assertThat(farePolicy.calculate(50)).isEqualTo(DEFAULT_FARE + 800 + 100);
    }
}