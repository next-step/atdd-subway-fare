package nextstep.subway.line.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.entity.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DurationTest {

    @Test
    @DisplayName("유효한 시간으로 Duration 객체를 생성한다.")
    void createValidDuration() {
        // given
        Long validDuration = 10L;

        // when
        Duration Duration = new Duration(validDuration);

        // then
        assertThat(Duration.getDuration()).isEqualTo(validDuration);
    }

    @Test
    @DisplayName("유효하지 않은 시간으로 Duration 객체 생성 시 예외가 발생한다.")
    void createInvalidDuration() {
        // given
        Long invalidDuration = -10L;

        // when, then
        assertThrows(SubwayException.class, () -> {
            new Duration(invalidDuration);
        });
    }

    @Test
    @DisplayName("유효한 시간으로 Duration 객체를 업데이트한다.")
    void updateValidDuration() {
        // given
        Duration Duration = new Duration(10L);
        Long newValidDuration = 20L;

        // when
        Duration.updateDuration(newValidDuration);

        // then
        assertThat(Duration.getDuration()).isEqualTo(newValidDuration);
    }

    @Test
    @DisplayName("유효하지 않은 시간으로 Duration 객체를 업데이트 시 예외가 발생한다.")
    void updateInvalidDuration() {
        // given
        Duration Duration = new Duration(10L);
        Long newInvalidDuration = -20L;

        // when, then
        assertThrows(SubwayException.class, () -> {
            Duration.updateDuration(newInvalidDuration);
        });
    }
}
