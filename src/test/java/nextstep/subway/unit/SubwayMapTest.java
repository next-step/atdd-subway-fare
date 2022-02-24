package nextstep.subway.unit;

import com.google.common.collect.Lists;
import nextstep.subway.domain.*;
import nextstep.subway.domain.fare.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static nextstep.subway.domain.fare.Fare.BASIC_FARE;
import static nextstep.subway.domain.fare.MemberDiscountPolicy.ADULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 판교역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    List<Line> lines;

    /**
     * * 노선(거리, 시간)
     *
     * 교대역      ---- *2호선(10, 3)*    ---- 강남역
     * |                                |
     * *3호선(2, 10)*                    *신분당선(10, 3)*
     * |                                |
     * 남부터미널역  ---- *3호선(3, 10)* ----  양재역
     *                                  |
     *                                  *신분당선(40, 20)*
     *                                  |
     *                                ----  판교역
     *
     * * (거리, 시간, 요금) [경로]
     *
     * 교대역 > 양재역
     * 최단 거리: (5, 20, 1250) [교대역, 남부터미널역, 양재역]
     * 최소 시간: (20, 6, 1250) [교대역, 강남역, 양재역]
     *
     * 교대역 > 판교역
     * 최단 거리: (45, 40, 1950) [교대역, 남부터미널역, 양재역, 판교역]
     * 최소 시간: (60, 26, 1950) [교대역, 강남역, 양재역, 판교역]
     */
    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        판교역 = createStation(5L, "판교역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 10, 3);
        신분당선.addSection(양재역, 판교역, 40, 20);
        이호선.addSection(교대역, 강남역, 10, 3);
        삼호선.addSection(교대역, 남부터미널역, 2, 10);
        삼호선.addSection(남부터미널역, 양재역, 3, 10);

        lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    }

    @Test
    void findPathMinDistance() {
        // given
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }

    @Test
    void findPathMinDistanceOppositely() {
        // given
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 남부터미널역, 교대역));
    }

    @Test
    void findPathMinDuration() {
        // given
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @Test
    void findPathMinDurationOppositely() {
        // given
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    }

    @Test
    void findPathBasicFare() {
        // given
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);
        Fare fare = path.calculateFare(ADULT.getAge());

        // then
        assertThat(path.extractDistance()).isEqualTo(5);
        assertThat(fare.getTotalFare()).isEqualTo(BASIC_FARE);
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }

    @DisplayName("경로가 10km를 초과하는 경우 초과 요금이 부과된다.")
    @Test
    void findPathMoreThan10km() {
        // given
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 판교역);
        Fare fare = path.calculateFare(ADULT.getAge());

        // then
        assertAll(
                () -> assertThat(path.extractDistance()).isEqualTo(45),
                () -> assertThat(fare.getTotalFare()).isEqualTo(1950),
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역, 판교역))
        );
    }

    @DisplayName("경로가 50km를 초과하는 경우 초과 요금이 부과된다.")
    @Test
    void findPathMoreThan50km() {
        // given
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(교대역, 판교역);
        Fare fare = path.calculateFare(ADULT.getAge());

        // then
        assertAll(
                () -> assertThat(path.extractDistance()).isEqualTo(60),
                () -> assertThat(fare.getTotalFare()).isEqualTo(1950),
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역, 판교역))
        );
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
