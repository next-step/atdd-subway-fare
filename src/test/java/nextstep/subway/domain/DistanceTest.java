package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.Duration.DURATION_NOT_NEGATIVE_AND_ZERO_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hibernate.type.IntegerType.ZERO;

@DisplayName("거리 관련 테스트")
class DistanceTest {

    private Distance distance;

    @BeforeEach
    void setUp() {
        distance = new Distance(10);
    }

    @DisplayName("0값 선언")
    @Test
    void zero() {
        assertThatThrownBy(() -> new Distance(ZERO), DURATION_NOT_NEGATIVE_AND_ZERO_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("음수값 선언")
    @Test
    void negative() {
        assertThatThrownBy(() -> new Distance(-1), DURATION_NOT_NEGATIVE_AND_ZERO_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("거리 빼기 계산")
    @Test
    void minus() {
        distance.minus(new Distance(3));
        assertThat(distance).isEqualTo(new Distance(7));
    }

    @DisplayName("거리 빼기 계산, 절대값 반환")
    @Test
    void minusNonNegative() {
        distance.minus(new Distance(15));
        assertThat(distance).isEqualTo(new Distance(5));
    }

    @DisplayName("거리 더하기 계산")
    @Test
    void plus() {
        distance.plus(new Distance(3));
        assertThat(distance).isEqualTo(new Distance(13));
    }

}