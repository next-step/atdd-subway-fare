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
        int fare = FarePolicy.calculateFare(9);
        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("10km 초과 요금 조회")
    @Test
    void over10KmFare() {
        int fare = FarePolicy.calculateFare(10);
        assertThat(fare).isEqualTo(DEFAULT_FARE + 100);
    }

    @DisplayName("49km 요금 조회")
    @Test
    void over49KmFare() {
        int fare = FarePolicy.calculateFare(49);
        assertThat(fare).isEqualTo(DEFAULT_FARE + 800);
    }

    @DisplayName("50km 초과 요금 조회")
    @Test
    void over50KmFare() {
        int fare = FarePolicy.calculateFare(50);
        assertThat(fare).isEqualTo(DEFAULT_FARE + 800 + 100);
    }
}