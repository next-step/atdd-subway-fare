package nextstep.subway.domain;

import nextstep.exception.line.MinimumDistanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("노선의 구간")
class SectionTest {

    private Station 강남역;
    private Station 정자역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        정자역 = new Station("정자역");
        신분당선 = new Line();
    }

    @DisplayName("구간의 거리는 1이상이어야 한다")
    @ValueSource(ints = {1, 2, 3, 4, 5, 10, 100, 500, 1000})
    @ParameterizedTest
    void validateDistance(int distance) {
        // when
        Section section = Section.of(신분당선, 강남역, 정자역, distance, 100);

        // then
        assertThat(section.getDistance()).isEqualTo(distance);
    }

    @DisplayName("구간의 거리가 1미만이라면 생성 실패한다")
    @ValueSource(ints = {-1000, -500, -100, -5, -4, -3, -2, -1})
    @ParameterizedTest
    void validateDistance_fail(int distance) {
        // then
        assertThatThrownBy(() -> Section.of(신분당선, 강남역, 정자역, distance, 100))
                .isInstanceOf(MinimumDistanceException.class);
    }

    @DisplayName("구간의 소요시간은 1이상이어야 한다")
    @ValueSource(ints = {1, 2, 3, 4, 5, 10, 100, 500, 1000})
    @ParameterizedTest
    void validateDuration(int duration) {
        // when
        Section section = Section.of(신분당선, 강남역, 정자역, 100, duration);

        // then
        assertThat(section.getDuration()).isEqualTo(duration);
    }

    @DisplayName("구간의 소요시간이 1미만이라면 생성 실패한다")
    @ValueSource(ints = {-1000, -500, -100, -5, -4, -3, -2, -1})
    @ParameterizedTest
    void validateDuration_fail(int duration) {
        // then
        assertThatThrownBy(() -> Section.of(신분당선, 강남역, 정자역, 100, duration))
                .isInstanceOf(MinimumDistanceException.class);
    }

}