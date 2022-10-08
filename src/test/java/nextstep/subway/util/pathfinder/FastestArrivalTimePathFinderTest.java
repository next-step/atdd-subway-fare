package nextstep.subway.util.pathfinder;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FastestArrivalTimePathFinderTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private List<Line> lines;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red", 0, LocalTime.of(7, 0), LocalTime.of(23, 0), 10);
        이호선 = new Line("2호선", "red", 0, LocalTime.of(9, 0), LocalTime.of(23, 30), 6);
        삼호선 = new Line("3호선", "red", 0, LocalTime.of(8, 0), LocalTime.of(22, 55), 7);

        신분당선.addSection(강남역, 양재역, 3, 10);
        이호선.addSection(교대역, 강남역, 3, 10);
        삼호선.addSection(교대역, 남부터미널역, 5, 1);
        삼호선.addSection(남부터미널역, 양재역, 5, 1);

        lines = List.of(이호선, 삼호선, 신분당선);
    }

    @DisplayName("가장 빨리 도착하는 경로를 조회한다.")
    @Test
    void findPath() {
        LocalDateTime startTime = LocalDateTime.of(2022, 10, 1, 14, 30);

        PathResponse path = FastestArrivalTimePathFinder.find(lines, startTime, 양재역, 교대역, 0);

        List<StationResponse> stations = List.of(StationResponse.of(양재역), StationResponse.of(남부터미널역), StationResponse.of(교대역));
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(stations),
                () -> assertThat(path.getArrivalTime()).isEqualTo(LocalDateTime.of(2022, 10, 1, 14, 34))
        );
    }

    @DisplayName("가장 빨리 도착하는 경로를 조회한다.(역방향)")
    @Test
    void findPathOppositely() {
        LocalDateTime startTime = LocalDateTime.of(2022, 10, 1, 14, 30);

        PathResponse path = FastestArrivalTimePathFinder.find(lines, startTime, 교대역, 양재역, 0);

        List<StationResponse> stations = List.of(StationResponse.of(교대역), StationResponse.of(남부터미널역), StationResponse.of(양재역));
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(stations),
                () -> assertThat(path.getArrivalTime()).isEqualTo(LocalDateTime.of(2022, 10, 1, 14, 34))
        );
    }

    @DisplayName("가장 빨리 도착하는 경로를 조회한다. (다음날)")
    @Test
    void findPathNextDay() {
        LocalDateTime startTime = LocalDateTime.of(2022, 10, 1, 23, 59);

        PathResponse path = FastestArrivalTimePathFinder.find(lines, startTime, 양재역, 교대역, 0);

        List<StationResponse> stations = List.of(StationResponse.of(양재역), StationResponse.of(남부터미널역), StationResponse.of(교대역));
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(stations),
                () -> assertThat(path.getArrivalTime()).isEqualTo(LocalDateTime.of(2022, 10, 2, 8, 2))
        );
    }

    @DisplayName("가장 빨리 도착하는 경로를 조회한다.(역방향, 다음날)")
    @Test
    void findPathOppositelyNextDay() {
        LocalDateTime startTime = LocalDateTime.of(2022, 10, 1, 23, 59);

        PathResponse path = FastestArrivalTimePathFinder.find(lines, startTime, 교대역, 양재역, 0);

        List<StationResponse> stations = List.of(StationResponse.of(교대역), StationResponse.of(남부터미널역), StationResponse.of(양재역));
        assertAll(
                () -> assertThat(path.getStations()).containsExactlyElementsOf(stations),
                () -> assertThat(path.getArrivalTime()).isEqualTo(LocalDateTime.of(2022, 10, 2, 8, 2))
        );
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
