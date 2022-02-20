package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PathTypeTest {
    private static int DISTANCE = 10;
    private static int DURATION = 20;

    @ParameterizedTest
    @MethodSource("pathTypeProvider")
    void getValue(PathType pathType, int expectedValue) {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Line 이호선 = new Line("2호선", "green");
        Section section = new Section(이호선, 교대역, 강남역, DISTANCE, DURATION);

        // when
        int value = pathType.getValue(section);

        // then
        assertThat(value).isEqualTo(expectedValue);
    }

    static Stream<Arguments> pathTypeProvider() {
        return Stream.of(
                Arguments.of(PathType.DISTANCE, DISTANCE),
                Arguments.of(PathType.DURATION, DURATION)
                );
    }
}