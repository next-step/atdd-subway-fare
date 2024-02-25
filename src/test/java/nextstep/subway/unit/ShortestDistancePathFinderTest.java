package nextstep.subway.unit;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.pathfinder.PathFinder;
import nextstep.subway.domain.pathfinder.ShortestDistancePathFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShortestDistancePathFinderTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 부천역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    @BeforeEach
    public void setUp() {
        교대역 = new Station(1L, "교대역");
        강남역 = new Station(2L, "강남역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        이호선 = new Line(1L, "2호선", "green", 교대역, 강남역, 10, 2);
        신분당선 = new Line(2L, "신분당선", "red", 강남역, 양재역, 10, 2);
        삼호선 = new Line(3L, "3호선", "orange", 교대역, 남부터미널역, 2, 10);
        삼호선.addSection(남부터미널역, 양재역, 3, 10);

        부천역 = new Station(5L, "부천역");
    }

    @DisplayName("최단 거리 경로를 조회하면 교대역 - 남부터미널역 - 양재역을 리턴한다.")
    @Test
    void pathFinder() {
        final PathFinder pathFinder = new ShortestDistancePathFinder();
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));
        final PathResponse pathResponse = pathFinder.findPath(sections, 교대역, 양재역);

        final List<StationResponse> stations = pathResponse.getStations();

        assertThat(stations.get(0).getName()).isEqualTo("교대역");
        assertThat(stations.get(1).getName()).isEqualTo("남부터미널역");
        assertThat(stations.get(2).getName()).isEqualTo("양재역");
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getDuration()).isEqualTo(20);
    }

    @DisplayName("최단 거리 경로 조회시, 출발역과 도착역이 동일하면 예외가 발생한다.")
    @Test
    void pathFinder_invalid_source_target_same() {
        final PathFinder pathFinder = new ShortestDistancePathFinder();
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));
        assertThatThrownBy(() -> { pathFinder.findPath(sections, 교대역, 교대역); })
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("최단 거리 경로 조회시, 출발역과 도착역이 연결되어 있지 않으면 예외가 발생한다.")
    @Test
    void pathFinder_invalid_source_target_disconnect() {
        final PathFinder pathFinder = new ShortestDistancePathFinder();
        final List<Section> sections = getSections(Arrays.asList(이호선, 신분당선, 삼호선));
        assertThatThrownBy(() -> { pathFinder.findPath(sections, 교대역, 부천역); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그래프에 존재하지 않는 정점입니다.");
    }

    private List<Section> getSections(final List<Line> lines) {
        return lines.stream()
                .flatMap(l -> l.getSections().stream())
                .distinct()
                .collect(Collectors.toList());
    }
}