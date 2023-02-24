package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.PathSearchType;
import nextstep.subway.domain.Station;

@ExtendWith(MockitoExtension.class)
class PathServiceMockTest {

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    private PathService pathService;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    /**
     * 교대역 ───── *2호선* ───── 강남역
     * │                        │
     * *3호선*                 *신분당선*
     * │                        │
     * 남부터미널역 ── *3호선* ───── 양재역
     */
    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService, stationService);

        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        ReflectionTestUtils.setField(교대역, "id", 1L);
        ReflectionTestUtils.setField(강남역, "id", 2L);
        ReflectionTestUtils.setField(양재역, "id", 3L);
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);

        이호선 = new Line("2호선", "green");
        신분당선 = new Line("신분당선", "red");
        삼호선 = new Line("3호선", "brown");
        ReflectionTestUtils.setField(이호선, "id", 1L);
        ReflectionTestUtils.setField(삼호선, "id", 2L);
        ReflectionTestUtils.setField(신분당선, "id", 3L);

        이호선.addSection(교대역, 강남역, 10, 5);
        신분당선.addSection(강남역, 양재역, 10, 5);
        삼호선.addSection(교대역, 남부터미널역, 5, 8);
        삼호선.addSection(남부터미널역, 양재역, 5, 8);

        when(stationService.findById(교대역.getId())).thenReturn(교대역);
        when(stationService.findById(양재역.getId())).thenReturn(양재역);
        when(lineService.findLines()).thenReturn(List.of(이호선, 신분당선, 삼호선));
    }

    @DisplayName("최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), PathSearchType.DISTANCE, 20);

        List<String> stations = path.getStations().stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(stations).containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName()),
            () -> assertThat(path.getDistance()).isEqualTo(10),
            () -> assertThat(path.getDuration()).isEqualTo(16),
            () -> assertThat(path.getFare()).isEqualTo(1_250)
        );

    }

    @DisplayName("최단 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), PathSearchType.DURATION, 20);

        List<String> stations = path.getStations().stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(stations).containsExactly(교대역.getName(), 강남역.getName(), 양재역.getName()),
            () -> assertThat(path.getDistance()).isEqualTo(20),
            () -> assertThat(path.getDuration()).isEqualTo(10),
            () -> assertThat(path.getFare()).isEqualTo(1_450)
        );
    }
}
