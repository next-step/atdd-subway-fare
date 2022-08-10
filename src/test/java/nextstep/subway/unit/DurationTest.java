package nextstep.subway.unit;

import nextstep.subway.domain.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DurationTest {

    @Test
    void createDuration() {
        Duration duration = Duration.from(10);
        assertThat(duration.getDuration()).isEqualTo(10);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void zeroOrNegativeDuration(int duration) {
        assertThatIllegalArgumentException().isThrownBy(() -> Duration.from(duration))
                .withMessage("소요 시간은 0 이하일 수 없습니다. 입력된 시간 : " + duration);
    }
}
