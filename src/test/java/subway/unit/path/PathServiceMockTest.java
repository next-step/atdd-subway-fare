package subway.unit.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.LineService;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.application.PathFinder;
import subway.path.application.PathFinderFactory;
import subway.path.application.PathService;
import subway.path.application.dto.PathRetrieveResponse;
import subway.path.domain.PathRetrieveType;
import subway.station.application.StationService;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("PathService 단위 테스트 (stub)")
@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {
    @Mock
    private StationService stationService;
    @Mock
    private LineService lineService;
    @Mock
    private PathFinderFactory pathFinderFactory;
    @InjectMocks
    private PathService pathService;

    /**
     * Given 구간이 있을 때
     * When 경로 조회를 하면
     * Then 경로 내 역이 목록으로 반환된다
     * Then 최단거리 경로의 총 길이가 반환된다
     */
    @DisplayName("최단거리 경로 조회")
    @Test
    void getShortestDistancePath() {
        // given
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        Station 선릉역 = new Station(3L, "선릉역");
        Line 이호선 = Line.builder().name("이호선").color("random").build();
        Section 이호선_1구간 = Section.builder().id(1L).upStation(강남역).downStation(역삼역).distance(5L).duration(5L).build();
        이호선.addSection(이호선_1구간);
        Section 이호선_2구간 = Section.builder().id(2L).upStation(역삼역).downStation(선릉역).distance(5L).duration(5L).build();
        이호선.addSection(이호선_2구간);

        List<StationResponse> stationResponses = List.of(StationResponse.from(강남역), StationResponse.from(역삼역), StationResponse.from(선릉역));
        PathRetrieveResponse response = PathRetrieveResponse.builder().distance(10L).stations(stationResponses).build();
        PathFinder pathFinder = mock(PathFinder.class);
        when(pathFinderFactory.createFinder(PathRetrieveType.DISTANCE)).thenReturn(pathFinder);

        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(3L)).thenReturn(선릉역);
        when(lineService.findByStation(강남역, 선릉역)).thenReturn(List.of(이호선));
        when(pathFinder.findPath(이호선.getLineSections().getSections(), 강남역, 선릉역)).thenReturn(response);

        // when
        PathRetrieveResponse shortestPath = pathService.getPath(강남역.getId(), 선릉역.getId(), PathRetrieveType.DISTANCE);

        // then
        assertThat(shortestPath.getStations())
                .extracting(StationResponse::getName)
                .containsExactlyInAnyOrder("강남역", "역삼역", "선릉역");

        // then
        assertThat(shortestPath.getDistance()).isEqualTo(10L);
    }

    /**
     * Given 구간이 있을 때
     * When 경로 조회를 하면
     * Then 경로 내 역이 목록으로 반환된다
     * Then 최소시간 경로의 총 길이가 반환된다
     */
    @DisplayName("최소시간 경로 조회")
    @Test
    void getMinimumTimePath() {
        // given
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        Station 선릉역 = new Station(3L, "선릉역");
        Line 이호선 = Line.builder().name("이호선").color("random").build();
        Section 이호선_1구간 = Section.builder().id(1L).upStation(강남역).downStation(역삼역).distance(5L).duration(5L).build();
        이호선.addSection(이호선_1구간);
        Section 이호선_2구간 = Section.builder().id(2L).upStation(역삼역).downStation(선릉역).distance(5L).duration(5L).build();
        이호선.addSection(이호선_2구간);

        List<StationResponse> stationResponses = List.of(StationResponse.from(강남역), StationResponse.from(역삼역), StationResponse.from(선릉역));
        PathRetrieveResponse response = PathRetrieveResponse.builder().distance(10L).duration(10L).stations(stationResponses).build();
        PathFinder pathFinder = mock(PathFinder.class);
        when(pathFinderFactory.createFinder(PathRetrieveType.DURATION)).thenReturn(pathFinder);

        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(3L)).thenReturn(선릉역);
        when(lineService.findByStation(강남역, 선릉역)).thenReturn(List.of(이호선));
        when(pathFinder.findPath(이호선.getLineSections().getSections(), 강남역, 선릉역)).thenReturn(response);

        // when
        PathRetrieveResponse minimumTimePath = pathService.getPath(강남역.getId(), 선릉역.getId(), PathRetrieveType.DURATION);

        // then
        assertThat(minimumTimePath.getStations())
                .extracting(StationResponse::getName)
                .containsExactlyInAnyOrder("강남역", "역삼역", "선릉역");

        // then
        assertThat(minimumTimePath.getDuration()).isEqualTo(10L);
    }
}
