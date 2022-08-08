package nextstep.subway.unit;

import nextstep.subway.domain.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.Duration.DURATION_NOT_NEGATIVE_AND_ZERO_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hibernate.type.IntegerType.ZERO;

@DisplayName("시간 관련 테스트")
class DurationTest {

    private Duration duration;

    @BeforeEach
    void setUp() {
        duration = new Duration(10);
    }

    @DisplayName("0값 선언")
    @Test
    void zero() {
        assertThatThrownBy(() -> new Duration(ZERO), DURATION_NOT_NEGATIVE_AND_ZERO_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("음수값 선언")
    @Test
    void negative() {
        assertThatThrownBy(() -> new Duration(-1), DURATION_NOT_NEGATIVE_AND_ZERO_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시간 빼기 계산")
    @Test
    void minus() {
        duration.minus(new Duration(3));
        assertThat(duration).isEqualTo(new Duration(7));
    }

    @DisplayName("시간 빼기 계산, 절대값 반환")
    @Test
    void minusNonNegative() {
        duration.minus(new Duration(15));
        assertThat(duration).isEqualTo(new Duration(5));
    }

    @DisplayName("시간 더하기 계산")
    @Test
    void plus() {
        duration.plus(new Duration(3));
        assertThat(duration).isEqualTo(new Duration(13));
    }
}