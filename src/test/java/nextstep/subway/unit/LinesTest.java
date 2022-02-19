package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Lines;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.exception.SectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LinesTest {

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;

    Line 이호선;
    Line 신분당선;
    Line 삼호선;

    Section 교대역_강남역_구간;
    Section 강남역_양재역_구간;
    Section 남부터미널역_양재역_구간;
    Section 교대역_남부터미널역_구간;

    /**
     * 교대역    --- *2호선(10m, 3분)* ---     강남역
     * |                                    |
     * 3호선(2m, 10분)                       신분당선(10m, 2분)
     * |                                    |
     * 남부터미널역  --- *3호선(3m, 11분)* ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        이호선 = new Line("2호선", "초록색");
        신분당선 = new Line("신분당선", "분홍색");
        삼호선 = new Line("삼호선", "주황색");

        교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 10, 2);
        남부터미널역_양재역_구간 = new Section(삼호선, 남부터미널역, 양재역, 3, 11);
        교대역_남부터미널역_구간 = new Section(삼호선, 교대역, 남부터미널역, 2, 10);

        이호선.addSection(교대역_강남역_구간);
        신분당선.addSection(강남역_양재역_구간);
        삼호선.addSection(남부터미널역_양재역_구간);
        삼호선.addSection(교대역_남부터미널역_구간);
    }

    @DisplayName("경로 찾기 시 시간 or 거리 중 기준이 아닌 시간 or 거리의 총합 반환")
    @Test
    void pathTotal() {
        // given
        Lines lines = new Lines(Arrays.asList(이호선, 신분당선, 삼호선));
        List<Station> stations = Arrays.asList(교대역, 남부터미널역, 양재역);

        // when
        int pathTotalDuration = lines.pathTotalDuration(stations);
        int pathTotalDistance = lines.pathTotalDistance(stations);

        // then
        assertThat(pathTotalDuration).isEqualTo(21);
        assertThat(pathTotalDistance).isEqualTo(5);
    }
}
