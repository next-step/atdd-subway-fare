package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /**       Distance
     *      강남역 --- (3) ---   양재역
     *       |                   |
     *       (3)                (4)
     *       |                   |
     *      교대역 --- (5) --- 남부터미널역*
     *
     *        Duration
     *      강남역 --- (3) ---   양재역
     *       |                   |
     *       (3)                (5)
     *       |                   |
     *      교대역 --- (4) --- 남부터미널역
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

        신분당선.addSection(강남역, 양재역, 3, 3);
        이호선.addSection(교대역, 강남역, 3, 3);
        삼호선.addSection(교대역, 남부터미널역, 5, 4);
        삼호선.addSection(남부터미널역, 양재역, 4, 5);
    }

    @Test
    void findPathByDistance() {
        // given
        SubwayMap map = new SubwayMap(List.of(신분당선, 이호선, 삼호선));

        // when
        Path path = map.findPath(강남역.getId(), 남부터미널역.getId(), PathSearchType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactly(강남역.getId(), 양재역.getId(), 남부터미널역.getId());
        assertThat(path.getDistance()).isEqualTo(7);
        assertThat(path.getDuration()).isEqualTo(8);
    }

    @Test
    void findPathByDuration() {
        // given
        SubwayMap map = new SubwayMap(List.of(신분당선, 이호선, 삼호선));

        // when
        Path path = map.findPath(강남역.getId(), 남부터미널역.getId(), PathSearchType.DURATION);

        // then
        assertThat(path.getStations()).containsExactly(강남역.getId(), 교대역.getId(), 남부터미널역.getId());
        assertThat(path.getDuration()).isEqualTo(7);
        assertThat(path.getDistance()).isEqualTo(8);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}