package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선 추가 요금 관련 기능")
class AdditionalFareTest {

    @DisplayName("노선은 0 미만일 수 없습니다.")
    @Test
    void create() {
        assertAll(
                () -> assertThatThrownBy(() -> new AdditionalFare(-1))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatCode(() -> new AdditionalFare(0)).doesNotThrowAnyException()
        );
    }

    @Test
    void equals() {
        assertThat(new AdditionalFare(1)).isEqualTo(new AdditionalFare(1));
    }
}
