package nextstep.subway.unit;

import com.google.common.collect.Lists;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayMapTest {

    private Station 교대역, 강남역, 양재역, 남부터미널역;
    private Line 신분당선, 이호선, 삼호선;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(new Section.Builder()
                                    .line(신분당선)
                                    .upStation(강남역)
                                    .downStation(양재역)
                                    .distance(3)
                                    .duration(3)
                                    .build());
        이호선.addSection(new Section.Builder()
                                    .line(이호선)
                                    .upStation(교대역)
                                    .downStation(강남역)
                                    .distance(3)
                                    .duration(3)
                                    .build());
        삼호선.addSection(new Section.Builder()
                                    .line(삼호선)
                                    .upStation(교대역)
                                    .downStation(남부터미널역)
                                    .distance(5)
                                    .duration(1)
                                    .build());
        삼호선.addSection(new Section.Builder()
                                    .line(삼호선)
                                    .upStation(남부터미널역)
                                    .downStation(양재역)
                                    .distance(5)
                                    .duration(2)
                                    .build());
    }

    @Test
    void findPath() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
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
