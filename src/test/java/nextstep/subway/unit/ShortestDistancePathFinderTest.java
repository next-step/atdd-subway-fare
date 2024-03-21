package nextstep.subway.unit;

import nextstep.exception.PathSourceTargetNotConnectedException;
import nextstep.exception.PathSourceTargetSameException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.pathfinder.ShortestDistancePathFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("최단 거리 찾기 테스트")
public class ShortestDistancePathFinderTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
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
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        이호선 = Line.builder().name("2호선").color("GREEN").upStation(강남역).downStation(교대역).distance(3L).duration(5L).additionalFare(0L).build();
        신분당선 = Line.builder().name("신분당선").color("RED").upStation(강남역).downStation(양재역).distance(10L).duration(5L).additionalFare(500L).build();
        삼호선 = Line.builder().name("3호선").color("ORANGE").upStation(교대역).downStation(남부터미널역).distance(2L).duration(5L).additionalFare(900L).build();

        삼호선.addSection(Section.builder()
                .line(삼호선)
                .upStation(남부터미널역)
                .downStation(양재역)
                .distance(3L)
                .duration(5L)
                .build());
    }

    @DisplayName("지하철 최단거리를 조회한다.")
    @Test
    void 지하철경로_조회() {
        ShortestDistancePathFinder pathFinder = new ShortestDistancePathFinder(Arrays.asList(이호선, 신분당선, 삼호선));
        Path path = pathFinder.findPath(교대역, 양재역);

        assertThat(path.getPath()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.getDistance()).isEqualTo(5L);
    }

    @DisplayName("출발역과 도착역이 같으면 에러가 발생한다.")
    @Test
    void 출발역과_도착역이_같은_경로_조회_애러() {
        ShortestDistancePathFinder pathFinder = new ShortestDistancePathFinder(Arrays.asList(이호선, 신분당선, 삼호선));

        assertThatThrownBy(() -> pathFinder.findPath(교대역, 교대역))
                .isInstanceOf(PathSourceTargetSameException.class);
    }

    @DisplayName("출발역과 도착역이 연결되어있지 않으면 에러가 발생한다.")
    @Test
    void 출발역과_도착역이_연결되어있_않으면_에러() {
        Station 신림역 = new Station("신림역");
        Station 보라매역 = new Station("보라매역");
        Station 사당역 = new Station("사당역");
        Station 이수역 = new Station("이수역");
        Line 신림선 = Line.builder().name("신림선").color("GRAY").upStation(신림역).downStation(보라매역).distance(2L).duration(10L).build();
        Line 사호선 = Line.builder().name("4호선").color("BLUE").upStation(사당역).downStation(이수역).distance(2L).duration(10L).build();

        ShortestDistancePathFinder pathFinder = new ShortestDistancePathFinder(Arrays.asList(신림선, 사호선));

        assertThatThrownBy(() -> pathFinder.findPath(신림역, 사당역))
                .isInstanceOf(PathSourceTargetNotConnectedException.class);
    }

}
