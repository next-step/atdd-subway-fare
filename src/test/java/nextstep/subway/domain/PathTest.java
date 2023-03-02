package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class PathTest {

    @DisplayName("거리에 따라 추가요금이 상이하게 부과된다.")
    @ParameterizedTest(name = "거리가 {0}km일 때 요금은 총 {1}원이다.")
    @MethodSource("distanceSource")
    void fares_vary_depending_on_the_distance(int distance, int fare) {
        // given
        Sections sections = mock(Sections.class);
        given(sections.totalDistance()).willReturn(distance);

        Path path = new Path(sections);

        // when
        int sut = path.getFare();

        // then
        assertThat(sut).isEqualTo(fare);
    }


    private static Stream<Arguments> distanceSource() {
        return Stream.of(
            Arguments.of(9, 1250),
            Arguments.of(10, 1250),
            Arguments.of(15, 1350),
            Arguments.of(16, 1450),
            Arguments.of(50, 2050),
            Arguments.of(58, 2150),
            Arguments.of(59, 2250)
        );
    }
}
