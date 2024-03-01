package nextstep.subway.unit;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.path.CalculateHandler;
import nextstep.subway.domain.path.DistanceCalculateHandler;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.path.ShortestDistancePathFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShortestDistancePathFinderTest extends PathFinderTest {

    @DisplayName("최단 거리 경로를 조회 하였을 때, 10km 이하면 1250원을 리턴한다.")
    @Test
    void pathFinder_fee() {
        삼호선 = new Line(3L, "3호선", "orange", 교대역, 남부터미널역, 5, 10);
        삼호선.addSection(남부터미널역, 양재역, 5, 10);
        final PathFinder pathFinder = new ShortestDistancePathFinder(new DistanceCalculateHandler(null));
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));

        final PathResponse pathResponse = pathFinder.findPath(sections, 교대역, 양재역);

        final PathResponse expectedPathResponse = new PathResponse(List.of(교대역, 남부터미널역, 양재역), 10, 20, 1250);
        verifyPathResponse(pathResponse, expectedPathResponse);
    }

    @DisplayName("최단 거리 경로를 조회 하였을 때, 10km 초과하면 1350원을 리턴한다.")
    @Test
    void pathFinder_fee_over_10Km() {
        삼호선 = new Line(3L, "3호선", "orange", 교대역, 남부터미널역, 6, 10);
        삼호선.addSection(남부터미널역, 양재역, 5, 10);
        final PathFinder pathFinder = new ShortestDistancePathFinder(new DistanceCalculateHandler(null));
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));

        final PathResponse pathResponse = pathFinder.findPath(sections, 교대역, 양재역);

        final PathResponse expectedPathResponse = new PathResponse(List.of(교대역, 남부터미널역, 양재역), 11, 20, 1350);
        verifyPathResponse(pathResponse, expectedPathResponse);
    }

    @DisplayName("최단 거리 경로를 조회 하였을 때, 50km 초과하면 2150원을 리턴한다.")
    @Test
    void pathFinder_fee_over_50Km() {
        이호선 = new Line(1L, "2호선", "green", 교대역, 강남역, 1000, 2);
        신분당선 = new Line(2L, "신분당선", "red", 강남역, 양재역, 1000, 2);
        삼호선 = new Line(3L, "3호선", "orange", 교대역, 남부터미널역, 25, 10);
        삼호선.addSection(남부터미널역, 양재역, 26, 10);

        final PathFinder pathFinder = new ShortestDistancePathFinder(new DistanceCalculateHandler(null));
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));

        final PathResponse pathResponse = pathFinder.findPath(sections, 교대역, 양재역);

        final PathResponse expectedPathResponse = new PathResponse(List.of(교대역, 남부터미널역, 양재역), 51, 20, 2150);
        verifyPathResponse(pathResponse, expectedPathResponse);
    }

    @DisplayName("최단 거리 경로 조회시, 출발역과 도착역이 동일하면 예외가 발생한다.")
    @Test
    void pathFinder_invalid_source_target_same() {
        final PathFinder pathFinder = new ShortestDistancePathFinder(new DistanceCalculateHandler(null));
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));

        assertThatThrownBy(() -> { pathFinder.findPath(sections, 교대역, 교대역); })
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("최단 거리 경로 조회시, 출발역과 도착역이 연결되어 있지 않으면 예외가 발생한다.")
    @Test
    void pathFinder_invalid_source_target_disconnect() {
        final PathFinder pathFinder = new ShortestDistancePathFinder(new DistanceCalculateHandler(null));
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));

        assertThatThrownBy(() -> { pathFinder.findPath(sections, 교대역, 부천역); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간에 포함되지 않은 지하철역: " + 부천역.getName());
    }
}
