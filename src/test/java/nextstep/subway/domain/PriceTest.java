package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import nextstep.subway.exception.CreateException;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void createException() {
        assertThatExceptionOfType(CreateException.class)
            .isThrownBy(() -> Price.of(-1))
            .withMessage("추가 요금은 음수가 될 수 없습니다.");
    }

}