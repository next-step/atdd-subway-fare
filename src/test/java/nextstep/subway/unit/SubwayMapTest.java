package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 3, 5);
        이호선.addSection(교대역, 강남역, 3, 5);
        삼호선.addSection(교대역, 남부터미널역, 5, 3);
        삼호선.addSection(남부터미널역, 양재역, 5, 3);
    }

    @DisplayName("최단 거리 기준으로 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역)),
                () -> assertThat(path.extractDistance()).isEqualTo(6),
                () -> assertThat(path.extractDuration()).isEqualTo(10)
        );
    }

    @DisplayName("최단 거리 기준으로 경로를 역 순으로 조회한다.")
    @Test
    void findPathOppositelyByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역)),
                () -> assertThat(path.extractDistance()).isEqualTo(6),
                () -> assertThat(path.extractDuration()).isEqualTo(10)
        );
    }

    @DisplayName("최단 시간 기준으로 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역)),
                () -> assertThat(path.extractDistance()).isEqualTo(10),
                () -> assertThat(path.extractDuration()).isEqualTo(6)
        );
    }

    @DisplayName("최단 시간 기준으로 경로를 역 순으로 조회한다.")
    @Test
    void findPathOppositelyByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 남부터미널역, 교대역)),
                () -> assertThat(path.extractDistance()).isEqualTo(10),
                () -> assertThat(path.extractDuration()).isEqualTo(6)
        );
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
