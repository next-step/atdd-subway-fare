package nextstep.subway.unit;

import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Duration;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.util.Adult;
import nextstep.subway.util.Children;
import nextstep.subway.util.Teenager;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
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
     * 교대역    --- *2호선*(0원, 3km, 3분)   ---     강남역
     * |                                                |
     * *3호선*(500원, 10km, 5분)                     *신분당선* (1000원, 3km, 3분)
     * |                                                 |
     * 남부터미널역  --- *3호선*(500원, 10km, 5분) ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red", Fare.from(1_000));
        이호선 = new Line("2호선", "red", Fare.from(0));
        삼호선 = new Line("3호선", "red", Fare.from(500));

        신분당선.addSection(createSectionBuilder(강남역, 양재역, Distance.from(3), Duration.from(3)));
        이호선.addSection(createSectionBuilder(교대역, 강남역, Distance.from(3), Duration.from(3)));
        삼호선.addSection(createSectionBuilder(교대역, 남부터미널역, Distance.from(10), Duration.from(5)));
        삼호선.addSection(createSectionBuilder(남부터미널역, 양재역, Distance.from(10), Duration.from(5)));
    }

    @Test
    void findPath() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, new Adult());

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역, new Adult());

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
        Path path = subwayMap.findPath(양재역, 교대역, new Adult());

        // then
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare();

        assertAll(
                () -> assertThat(distance).isEqualTo(6),
                () -> assertThat(duration).isEqualTo(6),
                () -> assertThat(fare).isEqualTo(2_250)
        );
    }

    /**
     * 교대역    --- *2호선*(0원, 3km, 3분)   ---     강남역
     * |                                                |
     * *3호선*(500원, 10km, 5분)                     *신분당선* (1000원, 3km, 3분)
     * |                                                 |
     * 남부터미널역  --- *3호선*(500원, 10km, 5분) ---   양재    ---   *신분당선* (1000원, 10km, 10분)   ---   모란역
     */
    @DisplayName("지하철 경로 조회 시 기본 운임 초과 조회")
    @Test
    void findPathOverTotalFare() {
        // given
        Station 모란역 = createStation(5L, "모란역");
        신분당선.addSection(createSectionBuilder(양재역, 모란역, Distance.from(10), Duration.from(10)));

        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 모란역, new Adult());

        // then
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare();

        assertAll(
                () -> assertThat(distance).isEqualTo(16),
                () -> assertThat(duration).isEqualTo(16),
                () -> assertThat(fare).isEqualTo(2_450)
        );
    }


    /**
     * 교대역    --- *2호선*(0원, 3km, 3분)   ---     강남역
     * |                                                |
     * *3호선*(500원, 10km, 5분)                     *신분당선* (1000원, 3km, 3분)
     * |                                                 |
     * 남부터미널역  --- *3호선*(500원, 10km, 5분) ---   양재    ---   *신분당선* (1000원, 65km, 10분)   ---   모란역
     */
    @DisplayName("거리가 50km 초과 시 운임 조회")
    @Test
    void findPathDistanceOver50KMFare() {
        // given
        Station 모란역 = createStation(5L, "모란역");
        신분당선.addSection(createSectionBuilder(양재역, 모란역, Distance.from(65), Duration.from(10)));

        List<Line> lines = Lists.newArrayList(신분당선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(강남역, 모란역, new Adult());

        // then
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare();

        assertAll(
                () -> assertThat(distance).isEqualTo(68),
                () -> assertThat(duration).isEqualTo(13),
                () -> assertThat(fare).isEqualTo(3350)
        );
    }

    @DisplayName("거리가 0인 구간을 추가할 때 예외 발생")
    @Test
    void addSectionExceptionWhenDistanceIsZero() {
        // given
        Station 모란역 = createStation(5L, "모란역");

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> 신분당선.addSection(createSectionBuilder(양재역, 모란역, Distance.from(0), Duration.from(5))))
                .withMessage("거리는 0 이하가 될 수 없습니다. 입력된 거리 : %d", 0);
    }

    @DisplayName("소요 시간이 0인 구간을 추가할 때 예외 발생")
    @Test
    void addSectionExceptionWhenDurationIsZero() {
        // given
        Station 모란역 = createStation(5L, "모란역");

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> 신분당선.addSection(createSectionBuilder(양재역, 모란역, Distance.from(5), Duration.from(0))))
                .withMessage("소요 시간은 0 이하일 수 없습니다. 입력된 시간 : %d", 0);
    }

    @DisplayName("경로에서 가장 값이 비싼 노선의 가격을 채택함")
    @Test
    void findMostExpensiveFareLine() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, new Adult());

        // then
        int lineFare = path.calculateMostExpensiveLine();
        assertThat(lineFare).isEqualTo(1_000);
    }

    /**
     * 교대역                                      남부터미널역
     * |                                            |
     * *a노선*(0원, 10km, 10분)                *c노선*(0원, 10km, 10분)
     * |                                           |
     * 강남역  --- *b노선*(0원, 10km, 10분) ---   양재
     */
    @DisplayName("경로의 추가 요금이 모두 0원 일 경우")
    @Test
    void allLineFareIsZero() {
        // given
        Line a노선 = new Line("A노선", "bg-red-600");
        Line b노선 = new Line("B노선", "bg-yellow-600");
        Line c노선 = new Line("C노선", "bg-blue-600");

        a노선.addSection(createSectionBuilder(교대역, 강남역, Distance.from(10), Duration.from(10)));
        b노선.addSection(createSectionBuilder(강남역, 양재역, Distance.from(10), Duration.from(10)));
        c노선.addSection(createSectionBuilder(양재역, 남부터미널역, Distance.from(10), Duration.from(10)));

        List<Line> lines = Lists.newArrayList(a노선, b노선, c노선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 남부터미널역, new Adult());

        // then
        assertAll(
                () -> assertThat(path.extractDistance()).isEqualTo(30),
                () -> assertThat(path.extractDuration()).isEqualTo(30),
                () -> assertThat(path.extractFare()).isEqualTo(1_750),
                () -> assertThat(path.calculateMostExpensiveLine()).isZero()
        );
    }

    @DisplayName("노선 생성 시 금액이 음수 값으로 들어올 경우 에러")
    @Test
    void negativeFareInLine() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Line("A노선", "bg-red-600", Fare.from(-1)))
                .withMessage("노선의 추가 요금은 0 미만의 금액이 들어올 수 없습니다. 입력된 금액 : %d", -1);
    }

    @DisplayName("빈 구간 리스트에서 값을 추출할 때 예외")
    @Test
    void noSectionResourceException() {
        Sections sections = new Sections();
        assertThatIllegalStateException().isThrownBy(sections::mostExpensiveLineFare)
                .withMessage("구간 리스트가 비어있습니다.");
    }

    @DisplayName("어린이 사용자가 로그인했을 때 경로 조회 시 요금이 할인된다")
    @Test
    void findPathDiscountWhenLoginChildren() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, new Children());

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역)),
                () -> assertThat(path.extractDistance()).isEqualTo(6),
                () -> assertThat(path.extractDuration()).isEqualTo(6),
                () -> assertThat(path.extractFare()).isEqualTo(1_300)
        );
    }

    @DisplayName("청소년 사용자가 로그인했을 때 경로 조회 시 요금이 할인된다")
    @Test
    void findPathDiscountWhenLoginTeenager() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, new Teenager());

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역)),
                () -> assertThat(path.extractDistance()).isEqualTo(6),
                () -> assertThat(path.extractDuration()).isEqualTo(6),
                () -> assertThat(path.extractFare()).isEqualTo(1_870)
        );
    }

    @DisplayName("성인 사용자가 로그인했을 때 경로 조회 시 요금은 할인되지 않는다")
    @Test
    void findPathNotDiscountWhenLoginAdult() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, new Adult());

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역)),
                () -> assertThat(path.extractDistance()).isEqualTo(6),
                () -> assertThat(path.extractDuration()).isEqualTo(6),
                () -> assertThat(path.extractFare()).isEqualTo(2_250)
        );
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

    private Line.SectionBuilder createSectionBuilder(Station upStation, Station downStation, Distance distance, Duration duration) {
        return new Line.SectionBuilder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .build();
    }
}
