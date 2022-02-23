package nextstep.subway.unit;

import com.google.common.collect.Lists;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubwayMapTest extends LineFixture{
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 서울역;
    private Station 부산역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 일호선;
    private Line 부산행;


    /**        (10Km, 5분)               (4km, 2분)           (20km, 120분)
     * 교대역    --- *2호선* ---   강남역  --- *1호선* --- 서울역 --- *부산행* --- 부산역
     * |                         |
     * (2km, 1분)            (10km, 3분)
     * *3호선*                 *신분당선*
     * |                         |
     * 남부터미널역  --- *3호선* --- 양재
     */
    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        서울역 = createStation(5L, "서울역");
        부산역 = createStation(6L, "부산역");

        신분당선 = createLine("신분당선", "red");
        이호선 = createLine("2호선", "red");
        삼호선 = createLine("3호선", "red");
        일호선 = createLine("1호선", "red", BigDecimal.valueOf(100));
        부산행 = createLine("KTX", "red", BigDecimal.valueOf(1000));

        신분당선.addSection(강남역, 양재역, 3, 1);
        이호선.addSection(교대역, 강남역, 3, 1);
        삼호선.addSection(교대역, 남부터미널역, 5, 2);
        삼호선.addSection(남부터미널역, 양재역, 5, 2);
        일호선.addSection(강남역, 서울역, 4, 2);
        부산행.addSection(서울역, 부산역, 20, 120);
    }

    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선, 일호선, 부산행);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역)),
                () -> assertThat(path.extractDistance()).isEqualTo(6)
        );
    }

    @DisplayName("최단 시간 경로를 찾는다.")
    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선, 일호선, 부산행);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(강남역, 남부터미널역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재역, 남부터미널역)),
                () -> assertThat(path.extractDuration()).isEqualTo(3)
        );
    }

    @DisplayName("최단 거리 경로를 찾는다.")
    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선, 일호선, 부산행);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    }

    @DisplayName("환승하는 노선 중에서 추가 운임이 있으면 그 중에서 최대 운임 비용을 확인한다.")
    @Test
    void maxAdditionalFare() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선, 일호선, 부산행);
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 부산역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 서울역, 부산역)),
                () -> assertThat(path.extractMaxAdditionalFare().compareTo(BigDecimal.valueOf(1000))).isEqualTo(0)
        );
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
