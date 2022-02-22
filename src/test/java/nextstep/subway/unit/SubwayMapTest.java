package nextstep.subway.unit;

import com.google.common.collect.Lists;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayMapTest extends LineFixture{
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

        신분당선 = createLine("신분당선", "red");
        이호선 = createLine("2호선", "red");
        삼호선 = createLine("3호선", "red");

        신분당선.addSection(강남역, 양재역, 3, 1);
        이호선.addSection(교대역, 강남역, 3, 1);
        삼호선.addSection(교대역, 남부터미널역, 5, 2);
        삼호선.addSection(남부터미널역, 양재역, 5, 2);
    }

    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        Assertions.assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역)),
                () -> assertThat(path.extractDistance()).isEqualTo(6)
        );
    }

    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(강남역, 남부터미널역);

        // then
        Assertions.assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재역, 남부터미널역)),
                () -> assertThat(path.extractDuration()).isEqualTo(3)
        );
    }

    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
