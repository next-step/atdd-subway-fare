package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *           (추가요금 100원)
 * 고속터미널역  --- *7호선* ---  강남구청역
 *    |
 *  *3호선*
 *    |
 *  교대역    --- *2호선*  ---  강남역
 *                             |
 *                         *신분당선* (추가 요금 300원)
 *                             |
 *                           양재역
 */
class SectionsTest {
    private Sections sections;

    // given
    @BeforeEach
    public void setUp() {
        Line 신분당선 = new Line("신분당선", "red", 300);
        Line 이호선 = new Line("2호선", "green");
        Line 삼호선 = new Line("3호선", "orange");
        Line 칠호선 = new Line("7호선", "dark green", 100);

        Station 양재역 = new Station("양재역");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 고속터미널역 = new Station("고속터미널역");
        Station 강남구청역 = new Station("강남구청역");

        sections = new Sections(
                List.of(
                        new Section(신분당선, 양재역, 강남역, 10, 3),
                        new Section(이호선, 강남역, 교대역, 10, 3),
                        new Section(삼호선, 교대역, 고속터미널역, 1, 3),
                        new Section(칠호선, 고속터미널역, 강남구청역, 3, 3)
                )
        );
    }

    @DisplayName("구간이 포함되어 있는 노선들 중, 가장 요금이 큰 노선의 요금을 가져온다.")
    @Test
    void getMaxExtraLineFare() {
        // when
        int extraFare = sections.extraFare();

        // then
        assertThat(extraFare).isEqualTo(300);
    }
}
