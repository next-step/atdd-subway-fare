package nextstep.subway.unit;

import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathType;
import nextstep.subway.domain.station.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선별 추가 요금")
public class LineExtraFareTest {

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

        신분당선 = new Line("신분당선", "red", 300);
        이호선 = new Line("2호선", "red", 0);
        삼호선 = new Line("3호선", "red", 500);

        신분당선.addSection(강남역, 양재역, 3, 5);
        이호선.addSection(교대역, 강남역, 4, 5);
        삼호선.addSection(교대역, 남부터미널역, 1, 5);
        삼호선.addSection(남부터미널역, 양재역, 1, 5);
    }

    /**
     * 교대역          --- *2호선 +0원 * (4KM, 5분) ---          강남역
     * |                                                        |
     *(1KM, 5분) *3호선 추가요금 500원*                 *신분당선 + 300원* (3KM, 5분)
     * |                                                        |
     * 남부터미널역   ---- *3호선 +500원* (1KM, 5분)-----        양재
     */
    @DisplayName("2호선 구간 추가 요금 0원 / 최단 거리 조회")
    @Test
    void line_extra_fare_0() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = SubwayMap.create(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 강남역, 20);

        assertThat(path.extractDistance()).isEqualTo(4);
        assertThat(path.extractDuration()).isEqualTo(5);
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역));
        assertThat(path.lineExtraFare()).isEqualTo(0);
    }

    /**
     * 교대역          --- *2호선 +0원 * (4KM, 5분) ---          강남역
     * |                                                        |
     *(1KM, 5분) *3호선 추가요금 500원*                 *신분당선 + 300원* (3KM, 5분)
     * |                                                        |
     * 남부터미널역   ---- *3호선 +500원* (1KM, 5분)-----        양재
     */
    @DisplayName("3호선 구간 추가 요금 900원 / 최단 거리 조회")
    @Test
    void line_extra_fare() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = SubwayMap.create(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, 20);

        assertThat(path.extractDistance()).isEqualTo(2);
        assertThat(path.extractDuration()).isEqualTo(10);
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertThat(path.lineExtraFare()).isEqualTo(500);
    }

    /**
     * 교대역          --- *2호선 +0원 * (4KM, 5분) ---          강남역
     * |                                                        |
     *(1KM, 5분) *3호선 추가요금 500원*                 *신분당선 + 300원* (3KM, 5분)
     * |                                                        |
     * 남부터미널역   ---- *3호선 +500원* (1KM, 5분)-----        양재
     */
    @DisplayName("가장 높은 구간 추가 요금 / 최단 거리 조회")
    @Test
    void line_multi_extra_fare() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = SubwayMap.create(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(남부터미널역, 강남역, 20);

        assertThat(path.extractDistance()).isEqualTo(4);
        assertThat(path.extractDuration()).isEqualTo(10);
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(남부터미널역, 양재역, 강남역));
        assertThat(path.lineExtraFare()).isEqualTo(500);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }

}
