package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {

    /**
     * 교대 -2호선- 강남 -신분당선- 양재
     */
    @DisplayName("추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용")
    @Test
    public void extra_fare() {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        Line 이호선 = new Line("2호선", "green", 600);
        Line 신분당선 = new Line("신분당선", "red", 1000);

        Section section1 = new Section(이호선, 교대역, 강남역, 10, 2);
        Section section2 = new Section(신분당선, 강남역, 양재역, 10, 3);

        Sections sections = new Sections(List.of(section1, section2));
        Path path = new Path(sections);

        // when
        int extraFare = path.extractMaxExtraFare();

        // then
        assertThat(extraFare).isEqualTo(1000);
    }
}
