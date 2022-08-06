package nextstep.subway.unit;

import nextstep.subway.domain.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 단위 테스트")
class SectionTest {

    private Section section = new Section(null, null, 10, 5);

    @DisplayName("거리 빼기 계산")
    @Test
    void minusDistance() {
        int result = section.minusDistance(new Section(null, null, 4, 3));

        assertThat(result).isEqualTo(6);
    }

    @DisplayName("거리 빼기 계산, 절대값 반환")
    @Test
    void minusDistanceNonNegative() {
        int result = section.minusDistance(new Section(null, null, 16, 3));

        assertThat(result).isEqualTo(6);
    }

    @DisplayName("거리 더하기 계산")
    @Test
    void plusDistance() {
        int result = section.plusDistance(new Section(null, null, 6, 3));

        assertThat(result).isEqualTo(16);
    }

    @DisplayName("시간 빼기 계산")
    @Test
    void minusDuration() {
        int result = section.minusDuration(new Section(null, null, 4, 2));

        assertThat(result).isEqualTo(3);
    }

    @DisplayName("시간 빼기 계산, 절대값 반환")
    @Test
    void minusDurationNonNegative() {
        int result = section.minusDuration(new Section(null, null, 16, 6));

        assertThat(result).isEqualTo(1);
    }

    @DisplayName("시간 더하기 계산")
    @Test
    void plusDuration() {
        int result = section.plusDuration(new Section(null, null, 6, 3));

        assertThat(result).isEqualTo(8);
    }

}