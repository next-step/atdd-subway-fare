package nextstep.subway.unit;

import nextstep.subway.constant.SearchType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red", 1000);
        이호선 = new Line("2호선", "red", 200);
        삼호선 = new Line("3호선", "red", 300);

        신분당선.addSection(강남역, 양재역, 3, 10);
        이호선.addSection(교대역, 강남역, 3, 1);
        삼호선.addSection(교대역, 남부터미널역, 5, 2);
        삼호선.addSection(남부터미널역, 양재역, 5, 2);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, SearchType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(path.extractDistance()).isEqualTo(6);
        assertThat(path.extractDuration()).isEqualTo(11);
        assertThat(path.getExtraFare()).isEqualTo(1000);
    }

    @DisplayName("두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, SearchType.DURATION);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertThat(path.extractDistance()).isEqualTo(10);
        assertThat(path.extractDuration()).isEqualTo(4);
        assertThat(path.getExtraFare()).isEqualTo(300);
    }

    @DisplayName("두 역의 최단 거리 경로를 반대로 조회한다.")
    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역, SearchType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
        assertThat(path.extractDistance()).isEqualTo(6);
        assertThat(path.extractDuration()).isEqualTo(11);
        assertThat(path.getExtraFare()).isEqualTo(1000);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
