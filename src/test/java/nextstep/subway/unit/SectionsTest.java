package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    Station 교대역;
    Station 양재역;
    Station 판교역;
    Station 남부터미널역;

    Line 신분당선;
    Line 삼호선;

    Sections sections;

    /**
     * * 노선(거리, 시간, 요금)
     *
     * 교대역
     * |
     * *3호선(2, 10, 300)*
     * |
     * 남부터미널역  ---- *3호선(3, 10, 300)* ----  양재역
     *                                     |
     *                                     *신분당선(40, 20, 100)*
     *                                     |
     *                                   ----  판교역
     * 교대역 > 판교역
     * 거리: 45
     * 요금: 1250 + 700 + 300 = 2250
     */
    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red", 100);
        삼호선 = new Line("3호선", "orange", 300);

        sections = new Sections();
        sections.add(new Section(신분당선, 양재역, 판교역, 40, 20));
        sections.add(new Section(삼호선, 교대역, 남부터미널역, 2, 10));
        sections.add(new Section(삼호선, 남부터미널역, 양재역, 3, 10));
    }

    @DisplayName("경로의 거리와 노선 요금 최댓값으로 총 요금을 계산한다.")
    @Test
    void calculateLineFare() {
        // when
        int lineFare = sections.totalFare();

        // then
        assertThat(lineFare).isEqualTo(2250);
    }
}