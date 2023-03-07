package nextstep.subway.unit;

import nextstep.subway.domain.DistanceFarePolicy;
import nextstep.subway.domain.FarePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.DistanceFarePolicy.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DistanceFarePolicyTest {

    private FarePolicy farePolicy;

    @BeforeEach
    void setUp() {
        farePolicy = new DistanceFarePolicy();
    }

    @Test
    @DisplayName("0 이하 이용거리")
    void lessThan0Distance() {
        // when
        // then
        assertThatThrownBy(() -> farePolicy.calculator(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 0 이하일 수 없습니다.");
    }

    @Test
    @DisplayName("기본 운임 비용")
    void defaultFare() {
        // when
        final int result = farePolicy.calculator(10);

        // then
        assertThat(result).isEqualTo(DEFAULT_FARE);
    }

    @Test
    @DisplayName("11~50km 운임 비용")
    void moreThan11LessThan50Fare() {
        // when
        final int result = farePolicy.calculator(50);

        // then
        assertThat(result).isEqualTo(DEFAULT_FARE + 800);
    }

    @Test
    @DisplayName("51km 이상 운임 비용")
    void moreThan51Fare() {
        // when
        final int result = farePolicy.calculator(58);

        // then
        assertThat(result).isEqualTo(DEFAULT_FARE + 800 + 100);
    }
}
