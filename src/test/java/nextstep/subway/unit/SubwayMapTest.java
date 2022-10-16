package nextstep.subway.unit;

import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.Fare;
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

@DisplayName("경로 조회 테스트")
public class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 고속터미널역;
    private Station 신사역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /*
     * 신사역
     * |
     * |  (46km, 5분)
     * |
     * 고속터미널역
     * |
     * |  (14km, 5분)
     * |
     * 교대역            --- *2호선*(3km, 3분) ---            강남역
     * |                                                       |
     * *3호선* + 500원 (5km, 5분)                   *신분당선* + 900원 (3km, 3분)
     * |                                                       |
     * 남부터미널역       --- *3호선* + 500원 (5km, 5분) ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        고속터미널역 = createStation(5L, "고속터미널역");
        신사역 = createStation(6L, "신사역");

        신분당선 = new Line("신분당선", "red", 900);
        이호선 = new Line("2호선", "red", 0);
        삼호선 = new Line("3호선", "red", 500);

        신분당선.addSection(강남역, 양재역, 3, 3);
        이호선.addSection(교대역, 강남역, 3, 3);
        삼호선.addSection(교대역, 남부터미널역, 5, 5);
        삼호선.addSection(남부터미널역, 양재역, 5, 5);
        삼호선.addSection(고속터미널역, 교대역, 14, 5);
        삼호선.addSection(신사역, 고속터미널역, 46, 5);
    }

    @DisplayName("최단 거리 구간 조회")
    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = SubwayMap.create(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.extractDistance()).isEqualTo(6);
        assertThat(path.extractDuration()).isEqualTo(6);
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @DisplayName("최단 거리 구간 조회 구간 거리 10 ~ 50")
    @Test
    void findFare_10km이상_50km이하() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = SubwayMap.create(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(양재역, 고속터미널역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역, 고속터미널역));
        assertThat(path.extractDistance()).isEqualTo(20);
        assertThat(new Fare(path.extractDistance(), path.getSections(), 20).calculate()).isEqualTo(2350);
    }

    @DisplayName("최단 거리 구간 조회 구간 거리 50km이상")
    @Test
    void findFare_50km이상() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = SubwayMap.create(lines, PathType.DISTANCE);

        // when
        Path path = subwayMap.findPath(양재역, 신사역);

        assertThat(path.extractDuration()).isEqualTo(16);
        assertThat(path.extractDistance()).isEqualTo(66);
        assertThat(new Fare(path.extractDistance(), path.getSections(), 20).calculate()).isEqualTo(3150);
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역, 고속터미널역, 신사역));

    }

    @DisplayName("최단 시간 구간 조회")
    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = SubwayMap.create(lines, PathType.DURATION);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.extractDistance()).isEqualTo(6);
        assertThat(path.extractDuration()).isEqualTo(6);
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = SubwayMap.create(lines, PathType.DISTANCE);

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
