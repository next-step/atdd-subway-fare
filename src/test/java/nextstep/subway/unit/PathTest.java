package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathTest {

    Line 이호선;
    Station 강남역;
    Station 양재역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "green");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
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

}