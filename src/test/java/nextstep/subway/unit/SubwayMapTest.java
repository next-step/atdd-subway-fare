package nextstep.subway.unit;

import com.google.common.collect.Lists;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.map.OneFieldSubwayMapGraphFactory;
import nextstep.subway.domain.map.OppositeOneFieldSubwayMapGraphFactory;
import nextstep.subway.domain.map.SubwayMap;
import nextstep.subway.domain.map.SubwayMapGraphFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 3, 3);
        이호선.addSection(교대역, 강남역, 3, 3);
        삼호선.addSection(교대역, 남부터미널역, 5, 1);
        삼호선.addSection(남부터미널역, 양재역, 5, 1);
        lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    }

    @DisplayName("거리를 기준으로 경로 탐색")
    @Test
    void findPathByDistance() {
        // given
        SubwayMapGraphFactory factory = new OneFieldSubwayMapGraphFactory(section -> (double) section.getDistance());

        // when
        Path path = subwayMap.findPath(factory.createGraph(lines), 교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(교대역, 강남역, 양재역);
    }

    @DisplayName("거리를 기준으로 반대로 경로 탐색")
    @Test
    void findPathByOppositeDistance() {
        // given
        SubwayMapGraphFactory factory = new OppositeOneFieldSubwayMapGraphFactory(section -> (double) section.getDistance());

        // when
        Path path = subwayMap.findPath(factory.createGraph(lines), 교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(양재역, 강남역, 교대역);
    }

    @DisplayName("소요 시간을 기준으로 경로 탐색")
    @Test
    void findPathByDuration() {
        // given
        SubwayMapGraphFactory factory = new OneFieldSubwayMapGraphFactory(section -> (double) section.getDuration());

        // when
        Path path = subwayMap.findPath(factory.createGraph(lines), 교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
