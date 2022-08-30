package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap.SubwayMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {
    private static final int 신분당선_경유_요금 = 2150;
    private static final int 일반_요금 = 1350;

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

        신분당선 = new Line("신분당선", "red", 900);
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 3, 4);
        이호선.addSection(교대역, 강남역, 3, 2);
        삼호선.addSection(교대역, 남부터미널역, 5, 2);
        삼호선.addSection(남부터미널역, 양재역, 5, 3);
    }

    @Test
    void findPathWithDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = PathType.DISTANCE.getInstance(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, 20, null);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(path.getFare()).isEqualTo(신분당선_경유_요금);
    }

    @Test
    void findPathWithDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = PathType.DURATION.getInstance(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, 20, null);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertThat(path.getFare()).isEqualTo(일반_요금);
    }

    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = PathType.DISTANCE.getInstance(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역, 20, null);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
        assertThat(path.getFare()).isEqualTo(신분당선_경유_요금);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}
