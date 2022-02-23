package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Lines;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {

    Station 교대역;
    Station 양재역;
    Station 남부터미널역;

    Line 이호선;
    Line 신분당선;
    Line 삼호선;

    Section 남부터미널역_양재역_구간;
    Section 교대역_남부터미널역_구간;

    @BeforeEach
    void setUp() {
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        교대역 = new Station("교대역");

        삼호선 = new Line("삼호선", "주황색");

        남부터미널역_양재역_구간 = new Section(삼호선, 남부터미널역, 양재역, 3, 11);
        교대역_남부터미널역_구간 = new Section(삼호선, 교대역, 남부터미널역, 2, 10);

        삼호선.addSection(남부터미널역_양재역_구간);
        삼호선.addSection(교대역_남부터미널역_구간);
    }

    @Test
    void pathTotal() {
        // given
        Lines lines = new Lines(Collections.singletonList(삼호선));

        // when
        int pathTotalDuration = lines.pathTotalDuration();
        int pathTotalDistance = lines.pathTotalDistance();

        // then
        assertThat(pathTotalDuration).isEqualTo(21);
        assertThat(pathTotalDistance).isEqualTo(5);
    }
}
