package nextstep.subway.unit.map;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.time.LocalTime;
import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.map.Path;
import nextstep.subway.domain.map.Paths;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.map.SectionEdge;
import nextstep.subway.domain.map.SubwayMap;
import nextstep.subway.domain.map.SubwayMapGraphFactory;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class SubwayMapTest {
    private SubwayMap subwayMap = new SubwayMap();

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private List<Line> lines;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        final LocalTime START_TIME = LocalTime.of(5, 0);
        final LocalTime END_TIME = LocalTime.of(23, 0);
        final LocalTime INTERVAL_TIME = LocalTime.of(0, 10);
        신분당선 = new Line("신분당선", "red", 0, START_TIME, END_TIME, INTERVAL_TIME);
        이호선 = new Line("2호선", "red", 0, START_TIME, END_TIME, INTERVAL_TIME);
        삼호선 = new Line("3호선", "red", 0, START_TIME, END_TIME, INTERVAL_TIME);

        신분당선.addSection(강남역, 양재역, 3, 3);
        이호선.addSection(교대역, 강남역, 3, 3);
        삼호선.addSection(교대역, 남부터미널역, 5, 1);
        삼호선.addSection(남부터미널역, 양재역, 5, 1);
        lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    }

    @DisplayName("거리를 기준으로 경로 탐색")
    @Test
    void findPathByDistance() {
        // Given
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = SubwayMapGraphFactory.newOneFieldWeightGraph(
            lines,
            eachEdge -> (double) eachEdge.getSection().getDistance()
        );

        // when
        Path path = subwayMap.findPath(graph, 교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(교대역, 강남역, 양재역);
    }

    @DisplayName("소요 시간을 기준으로 경로 탐색")
    @Test
    void findPathByDuration() {
        // given
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = SubwayMapGraphFactory.newOneFieldWeightGraph(
            lines, eachEdge -> (double) eachEdge.getSection().getDuration()
        );

        // when
        Path path = subwayMap.findPath(graph, 교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @DisplayName("경로 탐색 결과에 포함되는 Section이 Line을 가지고 있는지 확인")
    @Test
    void findPathResultIsLineNull() {
        // given
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = SubwayMapGraphFactory.newOneFieldWeightGraph(
            lines, eachEdge -> (double) eachEdge.getSection().getDistance()
        );

        // when
        Path path = subwayMap.findPath(graph, 교대역, 양재역);

        // then
        assertThat(path.getSections().getSections())
            .doesNotContainNull();
    }

    @DisplayName("도착 시간을 기준으로 모든 경로 탐색")
    @Test
    void findPathsByDuration() {
        // given
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = SubwayMapGraphFactory.newOneFieldWeightGraph(
            lines, eachEdge -> (double) eachEdge.getSection().getDuration()
        );

        // when
        Paths paths = subwayMap.findPaths(graph, 교대역, 양재역);

        // then
        assertThat(paths.size()).isEqualTo(2);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
