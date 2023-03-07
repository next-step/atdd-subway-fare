package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("시간 관련 기능")
class DurationTest {

    @DisplayName("시간은 1 이상이어야 한다.")
    @Test
    void name() {
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> new Duration(-1)),
                () -> assertThatThrownBy(() -> new Duration(0)),
                () -> assertDoesNotThrow(() -> new Duration(1))
        );
    }
}
