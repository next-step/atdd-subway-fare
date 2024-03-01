package nextstep.subway.unit;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.path.DistanceCalculateHandler;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.path.ShortestDurationPathFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShortestDurationPathFinderTest extends PathFinderTest {

    @DisplayName("최소 시간 경로를 조회하면 교대역 - 강남역 - 양재역을 리턴한다.")
    @Test
    void pathFinder() {
        final PathFinder pathFinder = new ShortestDurationPathFinder(new DistanceCalculateHandler(null));
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));

        final PathResponse pathResponse = pathFinder.findPath(sections, 교대역, 양재역);

        final PathResponse expectedPathResponse = new PathResponse(List.of(교대역, 강남역, 양재역), 20, 4, 1450);
        verifyPathResponse(pathResponse, expectedPathResponse);
    }

    @DisplayName("최소 시간 경로 조회시, 출발역과 도착역이 동일하면 예외가 발생한다.")
    @Test
    void pathFinder_invalid_source_target_same() {
        final PathFinder pathFinder = new ShortestDurationPathFinder(new DistanceCalculateHandler(null));
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));

        assertThatThrownBy(() -> { pathFinder.findPath(sections, 교대역, 교대역); })
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("최소 시간 경로 조회시, 출발역과 도착역이 연결되어 있지 않으면 예외가 발생한다.")
    @Test
    void pathFinder_invalid_source_target_disconnect() {
        final PathFinder pathFinder = new ShortestDurationPathFinder(new DistanceCalculateHandler(null));
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));

        assertThatThrownBy(() -> { pathFinder.findPath(sections, 교대역, 부천역); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간에 포함되지 않은 지하철역: " + 부천역.getName());
    }
}
