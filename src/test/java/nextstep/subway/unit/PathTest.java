package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Price;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    void path() {
        //given
        final Path path = new Path(new Sections(List.of(new Section(이호선, 강남역, 양재역, 5, 3))));

        //when
        final int fare = path.extractFare();

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

}