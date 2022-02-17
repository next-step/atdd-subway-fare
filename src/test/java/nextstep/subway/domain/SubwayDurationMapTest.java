package nextstep.subway.domain;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayDurationMapTest {

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

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 3, 30);
        이호선.addSection(교대역, 강남역, 3, 30);
        삼호선.addSection(교대역, 남부터미널역, 5, 50);
        삼호선.addSection(남부터미널역, 양재역, 5, 50);
    }

    @DisplayName("등록된 노선에서 목적지 까지 가장 빠른 시간의 경로를 찾는다")
    @Test
    void findPath() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayDistanceMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @DisplayName("등록된 노선에서 목적지 까지 반대 방향의 가장 빠른 시간의 경로를 찾는다")
    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayDistanceMap(lines);

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