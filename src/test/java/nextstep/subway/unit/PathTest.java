package nextstep.subway.unit;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.path.LeastDistanceFinder;
import nextstep.subway.domain.path.LeastTimeFinder;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import nextstep.subway.utils.SubwayFixture;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.utils.SubwayFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("경로 조회기능 단위 테스트")
public class PathTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 석남역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private List<Section> 구간_목록;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        석남역 = createStation(5L, "석남역");

        이호선 = createLine("2호선", "green", createSection(교대역, 강남역, 10, 8));
        신분당선 = createLine("신분당선", "green", createSection(강남역, 양재역, 10, 10));
        삼호선 = createLine("3호선", "green", createSection(교대역, 남부터미널역, 2, 4));
        삼호선.addSection(createSection(남부터미널역, 양재역, 3, 10));

        구간_목록 = List.of(이호선, 신분당선, 삼호선).stream()
                .map(Line::getSections)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @DisplayName("경로 탐색 성공 케이스")
    @Nested
    class successCase {
        @DisplayName("연결된 역에 대해 경로를 탐색하면 최단거리를 반환한다.")
        @Test
        void successFindEdge() {
            PathFinder pathFinder = new LeastDistanceFinder();
            GraphPath graphPath = pathFinder.findPath(교대역, 양재역, 구간_목록).get();
            List stations = graphPath.getVertexList();
            Long distance = (long) graphPath.getWeight();

            assertThat(stations.size()).isEqualTo(3);
            assertThat(distance).isEqualTo(5L);
        }
        // 기존 테스트는 남기고 신규 테스트로 TDD를 진행
        @DisplayName("연결된 역에 대해 경로를 탐색하면 최소 시간 기준 경로를 반환한다.")
        @Test
        void successFindEdgeByLeastTime() {
            PathFinder pathFinder = new LeastTimeFinder();
            GraphPath graphPath = pathFinder.findPath(교대역, 양재역, 구간_목록).get();
            List stations = graphPath.getVertexList();
            Long transitTime = (long) graphPath.getWeight();

            assertThat(stations.size()).isEqualTo(3);
            assertThat(transitTime).isEqualTo(14);
        }

        @DisplayName("연결된 역에 대해 경로를 탐색하면 경로와 함께 요금 정보를 반환한다.")
        @Test
        void fare() {
            // 경로 탐색
            PathFinder pathFinder = new LeastDistanceFinder();
            GraphPath graphPath = pathFinder.findPath(교대역, 양재역, 구간_목록).get();
            List stations = graphPath.getVertexList();
            Long distance = (long) graphPath.getWeight();

            // pathFinder의 getFare() 메서드를 직접 사용하기 위해 pathFinder 내부의 추상 메서드의 접근제어자를 public으로 변경
            // 테스트를 위해 default 접근 제어자를 public으로 변경하는 것이 과연 올바른 일일까?
            Long fare = pathFinder.getFare();

            assertThat(stations.size()).isEqualTo(3);
            assertThat(distance).isEqualTo(5L);
            assertThat(fare).isNotNull();
            assertThat(fare).isEqualTo(1250L);
        }
    }

    @DisplayName("경로 탐색 실패 케이스")
    @Nested
    class failCase {
        @DisplayName("출발역과 도착역이 같은 경우 구간 탐색에 실패한다")
        @Test
        void failFindPathWhenSameStations() {
            PathFinder pathFinder = new LeastDistanceFinder();
            assertThatThrownBy(() -> pathFinder.findPath(교대역, 교대역, 구간_목록).get().getVertexList())
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 구간 탐색에 실패한다.")
        @Test
        void failFindPathWhenNotConnected() {
            PathFinder pathFinder = new LeastDistanceFinder();
            assertThatThrownBy(() -> pathFinder.findPath(교대역, 석남역, 구간_목록).get().getVertexList())
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않은 출발역이나 도착역을 조회하는 경우 구간 탐색에 실패한다.")
        @Test
        void failFindPathWhenNotExist() {
            PathFinder pathFinder = new LeastDistanceFinder();
            assertThatThrownBy(() -> pathFinder.findPath(교대역, 교대역, 구간_목록).get().getVertexList())
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
