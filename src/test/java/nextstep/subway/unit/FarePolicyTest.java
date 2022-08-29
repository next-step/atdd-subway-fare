package nextstep.subway.unit;

import nextstep.subway.domain.policy.DistanceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.policy.DistanceType.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 정책 관련 테스트")
class FarePolicyTest {

    @DisplayName("10KM 요금 계산")
    @Test
    void defaultFare() {
        DistanceType distanceType = DistanceType.of(9);
        assertThat(distanceType.calculate(9)).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("10km 초과 요금 조회")
    @Test
    void over10KmFare() {
        DistanceType distanceType = DistanceType.of(10);
        assertThat(distanceType.calculate(10)).isEqualTo(DEFAULT_FARE + 100);
    }

    @DisplayName("49km 요금 조회")
    @Test
    void over49KmFare() {
        DistanceType distanceType = DistanceType.of(49);
        assertThat(distanceType.calculate(49)).isEqualTo(DEFAULT_FARE + 800);
    }

    @DisplayName("50km 초과 요금 조회")
    @Test
    void over50KmFare() {
        DistanceType distanceType = DistanceType.of(50);
        assertThat(distanceType.calculate(50)).isEqualTo(DEFAULT_FARE + 800 + 100);
    }
}