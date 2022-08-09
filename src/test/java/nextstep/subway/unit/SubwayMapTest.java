package nextstep.subway.unit;

import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Duration;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
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

        신분당선.addSection(강남역, 양재역, Distance.from(3), Duration.from(3));
        이호선.addSection(교대역, 강남역, Distance.from(3), Duration.from(3));
        삼호선.addSection(교대역, 남부터미널역, Distance.from(10), Duration.from(5));
        삼호선.addSection(남부터미널역, 양재역, Distance.from(10), Duration.from(5));
    }

    @Test
    void findPath() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    }

    @DisplayName("지하철 경로 조회 시 운임 조회")
    @Test
    void findPathTotalFare() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare();

        assertAll(
                () -> assertThat(distance).isEqualTo(6),
                () -> assertThat(duration).isEqualTo(6),
                () -> assertThat(fare).isEqualTo(1_250)
        );
    }

    @DisplayName("지하철 경로 조회 시 기본 운임 초과 조회")
    @Test
    void findPathOverTotalFare() {
        // given
        Station 모란역 = createStation(5L, "모란역");
        신분당선.addSection(양재역, 모란역, Distance.from(10), Duration.from(10));

        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 모란역);

        // then
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare();

        assertAll(
                () -> assertThat(distance).isEqualTo(16),
                () -> assertThat(duration).isEqualTo(16),
                () -> assertThat(fare).isEqualTo(1_450)
        );
    }

    @DisplayName("거리가 50km 초과 시 운임 조회")
    @Test
    void findPathDistanceOver50KMFare() {
        // given
        Station 모란역 = createStation(5L, "모란역");
        신분당선.addSection(양재역, 모란역, Distance.from(65), Duration.from(10));

        List<Line> lines = Lists.newArrayList(신분당선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(강남역, 모란역);

        // then
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare();

        assertAll(
                () -> assertThat(distance).isEqualTo(68),
                () -> assertThat(duration).isEqualTo(13),
                () -> assertThat(fare).isEqualTo(2350)
        );
    }

    @DisplayName("거리가 0인 구간을 추가할 때 예외 발생")
    @Test
    void addSectionExceptionWhenDistanceIsZero() {
        // given
        Station 모란역 = createStation(5L, "모란역");

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> 신분당선.addSection(양재역, 모란역, Distance.from(0), Duration.from(5)))
                .withMessage("거리는 0 이하가 될 수 없습니다. 입력된 거리 : %d", 0);
    }

    @DisplayName("소요 시간이 0인 구간을 추가할 때 예외 발생")
    @Test
    void addSectionExceptionWhenDurationIsZero() {
        // given
        Station 모란역 = createStation(5L, "모란역");

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> 신분당선.addSection(양재역, 모란역, Distance.from(5), Duration.from(0)))
                .withMessage("소요 시간은 0 이하일 수 없습니다. 입력된 시간 : %d", 0);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
