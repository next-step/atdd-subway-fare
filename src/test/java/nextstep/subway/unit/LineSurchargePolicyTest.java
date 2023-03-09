package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineSurchargePolicy;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineSurchargePolicyTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;
    private Station 삼성역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;
    private Sections sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        삼성역 = new Station("삼성역");

        이호선 = new Line("2호선", "green", 0);
        삼호선 = new Line("3호선", "orange", 500);
        신분당선 = new Line("신분당선", "red", 900);
    }

    @Test
    void extraFareAboutHighestSurchargeOfUsingLines() {
        sections = new Sections(
                List.of(
                        new Section(이호선, 강남역, 역삼역, 2, 2),
                        new Section(삼호선, 역삼역, 선릉역, 3, 3),
                        new Section(신분당선, 선릉역, 강남역, 4, 4)
                )
        );
        Path path = new Path(sections);

        assertThat(LineSurchargePolicy.calculate(path)).isEqualTo(신분당선.getSurcharge());
    }
}
