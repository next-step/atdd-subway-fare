package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("거리 관련 기능")
class DistanceTest {

    @DisplayName("거리는 1 이상이어야 한다.")
    @Test
    void create() {
        Assertions.assertAll(
                () -> assertThatThrownBy(() -> new Distance(-1)),
                () -> assertThatThrownBy(() -> new Distance(0)),
                () -> assertDoesNotThrow(() -> new Distance(1))
        );
    }
}
