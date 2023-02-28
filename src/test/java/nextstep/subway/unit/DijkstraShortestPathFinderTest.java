package nextstep.subway.unit;

import nextstep.common.exception.NoPathConnectedException;
import nextstep.common.exception.NoRegisterStationException;
import nextstep.common.exception.SameStationException;
import nextstep.subway.applicaion.DijkstraShortestPathFinder;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.common.error.SubwayError.NO_FIND_SAME_SOURCE_TARGET_STATION;
import static nextstep.common.error.SubwayError.NO_PATH_CONNECTED;
import static nextstep.common.error.SubwayError.NO_REGISTER_LINE_STATION;
import static nextstep.subway.acceptance.LineSteps.노선_생성;
import static nextstep.subway.acceptance.StationSteps.역_생성;
import static nextstep.subway.domain.SearchType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("최단 경로 찾기 다익스트라 알고리즘 기능")
class DijkstraShortestPathFinderTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 부평역;
    private Station 검암역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private Line 일호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {

        교대역 = 역_생성(1L, "교대역");
        강남역 = 역_생성(2L, "강남역");
        양재역 = 역_생성(3L, "양재역");
        남부터미널역 = 역_생성(4L, "남부터미널역");
        부평역 = 역_생성(5L, "부평역");
        검암역 = 역_생성(6L, "검암역");

        이호선 = 노선_생성(1L, "2호선", "green");
        이호선.addSection(교대역, 강남역, 10, 20);
        신분당선 = 노선_생성(2L, "신분당선", "red");
        신분당선.addSection(강남역, 양재역, 10, 20);
        삼호선 = 노선_생성(3L, "3호선", "orange");
        삼호선.addSection(교대역, 남부터미널역, 2, 4);
        삼호선.addSection(남부터미널역, 양재역, 3, 6);
        일호선 = 노선_생성(4L, "1호선", "blue");
        일호선.addSection(부평역, 검암역, 5, 10);
    }

    @DisplayName("최적의 경로를 찾는다.")
    @Test
    void findRouteTest() {
        final DijkstraShortestPathFinder pathFinder = new DijkstraShortestPathFinder();
        final List<Line> 전체_노선_목록 = Lists.newArrayList(신분당선, 이호선, 삼호선);
        final Path 경로_결과 = pathFinder.findPath(전체_노선_목록, 교대역, 양재역, DISTANCE);

        assertAll(
                () -> assertThat(경로_결과.getStations()).hasSize(3),
                () -> assertThat(경로_결과.getShortestDuration()).isEqualTo(10),
                () -> assertThat(경로_결과.getShortestDistance()).isEqualTo(5)
        );
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않아서 조회가 불가능합니다.")
    @Test
    void error_notConnectedSourceNTarget() {

        final DijkstraShortestPathFinder pathFinder = new DijkstraShortestPathFinder();
        final List<Line> 전체_노선_목록 = List.of(이호선, 신분당선, 삼호선, 일호선);

        assertThatThrownBy(() -> pathFinder.findPath(전체_노선_목록, 교대역, 부평역, DISTANCE))
                .isInstanceOf(NoPathConnectedException.class)
                .hasMessage(NO_PATH_CONNECTED.getMessage());
    }

    @DisplayName("출발역과 도착역이 같아서 조회가 불가능합니다.")
    @Test
    void error_sameSourceNTarget() {

        final DijkstraShortestPathFinder pathFinder = new DijkstraShortestPathFinder();
        final List<Line> 전체_노선_목록 = List.of(이호선, 신분당선, 삼호선);

        assertThatThrownBy(() -> pathFinder.findPath(전체_노선_목록, 교대역, 교대역, DISTANCE))
                .isInstanceOf(SameStationException.class)
                .hasMessage(NO_FIND_SAME_SOURCE_TARGET_STATION.getMessage());
    }

    @DisplayName("요청한 역이 노선의 등록되어 있지 않습니다.")
    @Test
    void error_noRegisterStation() {

        final DijkstraShortestPathFinder pathFinder = new DijkstraShortestPathFinder();
        final List<Line> 전체_노선_목록 = List.of(이호선, 신분당선, 삼호선);

        assertThatThrownBy(() -> pathFinder.findPath(전체_노선_목록, 부평역, 교대역, DISTANCE))
                .isInstanceOf(NoRegisterStationException.class)
                .hasMessage(NO_REGISTER_LINE_STATION.getMessage());
    }
}