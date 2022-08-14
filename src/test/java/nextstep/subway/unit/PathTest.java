package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Stream;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Price;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PathTest {

    Line 이호선;
    Line 삼호선;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "green");
        삼호선 = new Line("3호선", "orange", Price.of(1_000));
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
    }

    @Test
    @DisplayName("최단 경로 구하기")
    void path() {
        //given
        final Path path = new Path(new Sections(List.of(new Section(이호선, 강남역, 양재역, 5, 3))));

        //when
        final int fare = path.extractFare(20);

        //then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("노선 중 최고 가격 구하기")
    void maximumPrice() {
        //given
        final Path path = new Path(new Sections(List.of(
            new Section(이호선, 강남역, 양재역, 5, 3),
            new Section(삼호선, 강남역, 남부터미널역, 5, 3)
        )));

        //when
        final int price = path.maximumPrice();

        //then
        assertThat(price).isEqualTo(1_000);
    }

    @DisplayName("나이별 할인 요금")
    @ParameterizedTest
    @MethodSource
    void calculateFare(final int age, final int expected) {
        //given
        final Path path = new Path(new Sections(List.of(new Section(이호선, 강남역, 양재역, 5, 3))));

        //when
        final int fare = path.extractFare(age);

        //then
        assertThat(fare).isEqualTo(expected);
    }

    private static Stream<Arguments> calculateFare() {
        return Stream.of(
            Arguments.of(20, 1250),
            Arguments.of(19, 1070),
            Arguments.of(7, 800),
            Arguments.of(5, 0)
        );
    }

}