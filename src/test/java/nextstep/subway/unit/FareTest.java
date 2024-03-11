package nextstep.subway.unit;

import nextstep.subway.path.domain.FareCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {
    private static final int BASIC_FARE = 1_250;
    private static final int ADDITIONAL_FARE = 100;

    @DisplayName("거리에 따른 요금 조회")
    @Test
    void fare() {
        assertThat(FareCalculator.calculateFare(10)).isEqualTo(BASIC_FARE);
        assertThat(FareCalculator.calculateFare(50)).isEqualTo(BASIC_FARE + ADDITIONAL_FARE * 8);
        assertThat(FareCalculator.calculateFare(51)).isEqualTo(BASIC_FARE + ADDITIONAL_FARE * 8 + ADDITIONAL_FARE * 1);
    }
}
