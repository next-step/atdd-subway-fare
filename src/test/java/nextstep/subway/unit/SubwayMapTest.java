package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static nextstep.subway.fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /**
     *
     *
     *             3
     * 교대역  --- *2호선* ---   강남역
     * |                        |
     * 5  *3호선*                   *신분당선*    3
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *                  5
     */
    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red", 500);
        이호선 = new Line("2호선", "red", 400);
        삼호선 = new Line("3호선", "red", 900);

        신분당선.addSection(강남역, 양재역, 3, 5);
        이호선.addSection(교대역, 강남역, 3, 5);
        삼호선.addSection(교대역, 남부터미널역, 5, 3);
        삼호선.addSection(남부터미널역, 양재역, 5, 3);
    }

    @Test
    void findPath() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathSearchType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역, PathSearchType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    }

    @DisplayName("최소시간기준으로경로를 반환한다")
    @Test
    void 최소시간기준으로_경로를_반환한다() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathSearchType.DURATION);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }

    @DisplayName("경로중 노선의 가장높은 금액의 추가요금만 적용한다")
    @Test
    void 경로중_노선의_가장높은_금액의_추가요금만_적용한다() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathSearchType.DISTANCE);
        Fare fare = Fare.of(new DistanceFarePolicy(path.getMaxExtraFare()), path.getTotalDistance());

        // then
        assertThat(fare.get()).isEqualTo(DistanceFare.DEFAULT.getValue() + 500);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
