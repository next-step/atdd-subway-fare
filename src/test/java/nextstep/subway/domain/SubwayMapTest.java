package nextstep.subway.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest extends FareManagerLoaderTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        super.setUp();

        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red", 900);
        이호선 = new Line("2호선", "red", 0);
        삼호선 = new Line("3호선", "red", 0);

        신분당선.addSection(강남역, 양재역, 3, 5);
        이호선.addSection(교대역, 강남역, 3, 10);
        삼호선.addSection(교대역, 남부터미널역, 5, 5);
        삼호선.addSection(남부터미널역, 양재역, 6, 5);
    }

    @Test
    @DisplayName("상행 -> 하행 방향으로 최소 거리 경로를 탐색한다.")
    void findPath_minimum_distance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @Test
    @DisplayName("하행 -> 상행 방향으로 최소 거리 경로를 탐색한다.")
    void findPathOppositely_minimum_distance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    }

    @Test
    @DisplayName("상행 -> 하행 방향으로 최소 소요시간 경로를 탐색한다.")
    void findPath_minimum_duration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }

    @Test
    @DisplayName("하행 -> 상행 방향으로 최소 소요시간 경로를 탐색한다.")
    void findPathOppositely_minimum_duration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 남부터미널역, 교대역));
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
