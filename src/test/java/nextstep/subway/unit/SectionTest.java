package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionTest {
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;

    private Line line;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");

        line = new Line("2호선", "green");
    }

    @DisplayName("getWeightOf메서드 테스트")
    @ParameterizedTest
    @MethodSource(value = "provideTypeAndEtc")
    void getWeightOf(FindType type, int etc) {
        // given
        Section section = new Section(line, 강남역, 역삼역, 5, 10);

        // when & then
        assertThat(section.getWeightOf(type)).isEqualTo(etc);
    }

    private static Stream<Arguments> provideTypeAndEtc() {
        return Stream.of(
                Arguments.of(FindType.DISTANCE, 5),
                Arguments.of(FindType.DURATION, 10)
        );
    }
}
