package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.domain.PathType.DISTANCE;
import static nextstep.subway.domain.PathType.DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 검색 기능을 확인한다.")
public class SubwayMapTest extends UnitTestData {

    @DisplayName("최소 거리로 경로를 찾는다")
    @Test
    void findPathMinimumDistance() {
        // given
        /**
         * 교대역    --- *2호선(3km, 3분)* ---   강남역
         * |                                     |
         * *3호선(5km, 2분)*               *신분당선(3km, 3분)*
         * |                                     |
         * 남부터미널역  --- *3호선(5km, 2분)* ---   양재역
         */
        신분당선에_강남역_양재역_구간을_추가한다(3, 3);
        이호선에_교대역_강남역_구간을_추가한다(3, 3);
        삼호선에_교대역_남부터미널역_구간을_추가한다(5, 2);
        삼호선에_남부터미널역_양재역_구간을_추가한다(5, 2);

        SubwayMap subwayMap = getSubwayMap();

        // when
        Path path = subwayMap.findPath(교대역, 양재역, DISTANCE);

        // then
        경로를_검증한다(path, Lists.newArrayList(교대역, 강남역, 양재역), 6, 6, 1250);
    }

    @DisplayName("최소 시간으로 경로를 찾는다")
    @Test
    void findPathMinimumTime() {
        // given
        /**
         * 교대역    --- *2호선(3km, 3분)* ---   강남역
         * |                                     |
         * *3호선(5km, 2분)*               *신분당선(3km, 3분)*
         * |                                     |
         * 남부터미널역  --- *3호선(5km, 2분)* ---   양재역
         */
        신분당선에_강남역_양재역_구간을_추가한다(3, 3);
        이호선에_교대역_강남역_구간을_추가한다(3, 3);
        삼호선에_교대역_남부터미널역_구간을_추가한다(5, 2);
        삼호선에_남부터미널역_양재역_구간을_추가한다(5, 2);

        SubwayMap subwayMap = getSubwayMap();

        // when
        Path path = subwayMap.findPath(교대역, 양재역, DURATION);

        // then
        경로를_검증한다(path, Lists.newArrayList(교대역, 남부터미널역, 양재역), 10, 4, 1250);

    }

    @DisplayName("최소 거리로 경로를 반대로 찾는다")
    @Test
    void findPathOppositelyMinimumDistance() {
        // given
        /**
         * 교대역    --- *2호선(3km, 3분)* ---   강남역
         * |                                     |
         * *3호선(5km, 2분)*               *신분당선(3km, 3분)*
         * |                                     |
         * 남부터미널역  --- *3호선(5km, 2분)* ---   양재역
         */
        신분당선에_강남역_양재역_구간을_추가한다(3, 3);
        이호선에_교대역_강남역_구간을_추가한다(3, 3);
        삼호선에_교대역_남부터미널역_구간을_추가한다(5, 2);
        삼호선에_남부터미널역_양재역_구간을_추가한다(5, 2);

        SubwayMap subwayMap = getSubwayMap();

        // when
        Path path = subwayMap.findPath(양재역, 교대역, DISTANCE);

        // then
        경로를_검증한다(path, Lists.newArrayList(양재역, 강남역, 교대역), 6, 6, 1250);
    }

    @DisplayName("최소 시간으로 경로를 반대로 찾는다")
    @Test
    void findPathOppositelyMinimumTime() {
        // given
        /**
         * 교대역    --- *2호선(3km, 3분)* ---   강남역
         * |                                     |
         * *3호선(5km, 2분)*               *신분당선(3km, 3분)*
         * |                                     |
         * 남부터미널역  --- *3호선(5km, 2분)* ---   양재역
         */
        신분당선에_강남역_양재역_구간을_추가한다(3, 3);
        이호선에_교대역_강남역_구간을_추가한다(3, 3);
        삼호선에_교대역_남부터미널역_구간을_추가한다(5, 2);
        삼호선에_남부터미널역_양재역_구간을_추가한다(5, 2);

        SubwayMap subwayMap = getSubwayMap();

        // when
        Path path = subwayMap.findPath(양재역, 교대역, DURATION);

        // then
        경로를_검증한다(path, Lists.newArrayList(양재역, 남부터미널역, 교대역), 10, 4, 1250);
    }

    @DisplayName("거리가 50km 이하일 때, 총 가격을 검증한다.")
    @Test
    void calculatePriceUnder50() {
        //Given
        /**
         * 교대역    --- *2호선(30km, 15분)* ---   강남역
         * |                                     |
         * *3호선(25km, 12분)*               *신분당선(30km, 15분)*
         * |                                     |
         * 남부터미널역  --- *3호선(25km, 12분)* ---   양재역
         */
        신분당선에_강남역_양재역_구간을_추가한다(30, 15);
        이호선에_교대역_강남역_구간을_추가한다(30, 15);
        삼호선에_교대역_남부터미널역_구간을_추가한다(25, 12);
        삼호선에_남부터미널역_양재역_구간을_추가한다(25, 12);
        SubwayMap subwayMap = getSubwayMap();

        //when
        Path path = subwayMap.findPath(교대역, 양재역, DISTANCE);

        //then
        경로를_검증한다(path, Lists.newArrayList(교대역, 남부터미널역, 양재역), 50, 24, 2050);
    }

    @DisplayName("거리가 50km 초과일 때, 총 가격을 검증한다.")
    @Test
    void calculatePriceOver50() {
        //Given
        /**
         * 교대역    --- *2호선(30km, 15분)* ---   강남역
         * |                                     |
         * *3호선(75km, 32분)*               *신분당선(30km, 15분)*
         * |                                     |
         * 남부터미널역  --- *3호선(75km, 32분)* ---   양재역
         */
        신분당선에_강남역_양재역_구간을_추가한다(30, 15);
        이호선에_교대역_강남역_구간을_추가한다(30, 15);
        삼호선에_교대역_남부터미널역_구간을_추가한다(75, 32);
        삼호선에_남부터미널역_양재역_구간을_추가한다(75, 32);
        SubwayMap subwayMap = getSubwayMap();

        //when
        Path path = subwayMap.findPath(교대역, 양재역, DISTANCE);

        //then
        경로를_검증한다(path, Lists.newArrayList(교대역, 강남역, 양재역), 60, 30, 2250);
    }

    private SubwayMap getSubwayMap() {
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);
        return subwayMap;
    }

    private void 경로를_검증한다(Path path, ArrayList<Station> stations, int distance, int duration, int price) {
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(stations),
                () -> assertThat(path.extractDistance()).isEqualTo(distance),
                () -> assertThat(path.extractDuration()).isEqualTo(duration),
                () -> assertThat(path.calculatePrice()).isEqualTo(price)
        );
    }
}
