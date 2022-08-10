package nextstep.subway.unit;

import nextstep.subway.domain.Distance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DistanceTest {

    @Test
    void createDistance() {
        Distance distance = Distance.from(10);
        assertThat(distance.getDistance()).isEqualTo(10);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void negativeDistance(int distance) {
        assertThatIllegalArgumentException().isThrownBy(() -> Distance.from(distance))
                .withMessage("거리는 0 이하가 될 수 없습니다. 입력된 거리 : " + distance);
    }
}
