package nextstep.path.application.fare.discount.age;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class AgeRangeTest {

    @Test
    @DisplayName("나이의 범위를 설정해 포함 여부를 반환 받을 수 있다.")
    void containsTest() {
        final AgeRange ageRange = AgeRange.of(1, 2);

        assertThat(ageRange.contains(1)).isTrue();
    }

    @Test
    @DisplayName("시작나이나 종료나이는 음수가 될 수 없다.")
    void negativeAgeTest() {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> AgeRange.of(-1, 1))
                    .isInstanceOf(IllegalArgumentException.class);
            softly.assertThatThrownBy(() -> AgeRange.of(1, -1))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }
}
