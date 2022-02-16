package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.path.domain.SubwayMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /**          (10km, 10min)
     * 교대역    --- *2호선* ---   강남역
     *   |                          |
     * (2km, 2min)             (10km, 10min)
     * *3호선*                   *신분당선*
     *   |                          |
     * 남부터미널역  --- *3호선* --- 양재
     *               (3km, 3min)
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

        신분당선.addSection(강남역, 양재역, 10, 10);
        이호선.addSection(교대역, 강남역, 10, 10);
        삼호선.addSection(교대역, 남부터미널역, 2, 2);
        삼호선.addSection(남부터미널역, 양재역, 3, 3);
    }

    @ParameterizedTest(name = "두 역의 최소 경로를 조회한다. [{arguments}]")
    @EnumSource(PathType.class)
    void findPath(PathType type) {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, type);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertAll(() -> {
            assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
            assertThat(path.extractDistance()).isEqualTo(5);
            assertThat(path.extractDuration()).isEqualTo(5);
        });
    }

    @ParameterizedTest(name = "반대 방향으로 두 역의 최소 경로를 조회한다. [{arguments}]")
    @EnumSource(PathType.class)
    void findPathOppositely(PathType type) {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, type);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertAll(() -> {
            assertThat(path.getStations()).containsExactly(양재역, 남부터미널역, 교대역);
            assertThat(path.extractDistance()).isEqualTo(5);
            assertThat(path.extractDuration()).isEqualTo(5);
        });
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
