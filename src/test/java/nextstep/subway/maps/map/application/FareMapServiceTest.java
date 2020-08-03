package nextstep.subway.maps.map.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Maps;
import nextstep.subway.maps.line.application.LineService;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.map.dto.FarePathResponse;
import nextstep.subway.maps.map.dto.MapResponse;
import nextstep.subway.maps.station.application.StationService;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;

@ExtendWith(MockitoExtension.class)
public class FareMapServiceTest {

    private FareMapService fareMapService;

    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;
    @Mock
    private PathService pathService;
    @Mock
    private FareCalculator fareCalculator;

    private Map<Long, Station> stations;
    private List<Line> lines;

    private SubwayPath subwayPath;
    private SubwayPath shortestPath;

    private Station 교대역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        stations = Maps.newHashMap();
        교대역 = TestObjectUtils.createStation(1L, "교대역");
        Station 강남역 = TestObjectUtils.createStation(2L, "강남역");
        양재역 = TestObjectUtils.createStation(3L, "양재역");
        Station 남부터미널역 = TestObjectUtils.createStation(4L, "남부터미널역");

        stations.put(1L, 교대역);
        stations.put(2L, 강남역);
        stations.put(3L, 양재역);
        stations.put(4L, 남부터미널역);

        Line 서울_지하철_2호선 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0);
        LineStation 서울_지하철_2호선_교대역 = new LineStation(교대역.getId(), null, 0, 0);
        LineStation 서울_지하철_2호선_강남역 = new LineStation(강남역.getId(), 교대역.getId(), 2, 2);
        서울_지하철_2호선.addLineStation(서울_지하철_2호선_교대역);
        서울_지하철_2호선.addLineStation(서울_지하철_2호선_강남역);

        Line 신분당선 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0);
        LineStation 신분당선_강남역 = new LineStation(강남역.getId(), null, 0, 0);
        LineStation 신분당선_양재역 = new LineStation(양재역.getId(), 강남역.getId(), 2, 1);
        신분당선.addLineStation(신분당선_강남역);
        신분당선.addLineStation(신분당선_양재역);

        Line 서울_지하철_3호선 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0);
        LineStation 서울_지하철_3호선_교대역 = new LineStation(교대역.getId(), null, 0, 0);
        LineStation 서울_지하철_3호선_남부터미널역 = new LineStation(남부터미널역.getId(), 교대역.getId(), 1, 2);
        LineStation 서울_지하철_3호선_양재역 = new LineStation(양재역.getId(), 남부터미널역.getId(), 2, 2);

        서울_지하철_3호선.addLineStation(서울_지하철_3호선_교대역);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_남부터미널역);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_양재역);

        lines = Arrays.asList(서울_지하철_2호선, 서울_지하철_3호선, 신분당선);

        List<LineStationEdge> lineStations = Arrays.asList(
            new LineStationEdge(서울_지하철_3호선_남부터미널역, 서울_지하철_3호선),
            new LineStationEdge(서울_지하철_3호선_양재역, 서울_지하철_3호선)
        );

        List<LineStationEdge> shortestLineStations = Arrays.asList(
            new LineStationEdge(서울_지하철_2호선_교대역, 서울_지하철_2호선),
            new LineStationEdge(서울_지하철_2호선_강남역, 서울_지하철_2호선),
            new LineStationEdge(신분당선_강남역, 신분당선),
            new LineStationEdge(신분당선_양재역, 신분당선)
        );

        subwayPath = new SubwayPath(lineStations);
        shortestPath = new SubwayPath(shortestLineStations);
        fareMapService = new FareMapService(lineService, stationService, pathService, fareCalculator);
    }

    @DisplayName("최단 경로를 반환할 때, 기본 운임에 해당하는 값을 포함하여 반환할 수 있다.")
    @Test
    void 기본_운임을_반환한다() {
        // given
        when(lineService.findLines()).thenReturn(lines);
        when(pathService.findPath(anyList(), anyLong(), anyLong(), any())).thenReturn(subwayPath);

        when(stationService.findStationsByIds(anyList())).thenReturn(stations);
        when(fareCalculator.calculate(anyInt())).thenReturn(FareCalculator.BASIC_FARE);

        // when
        FarePathResponse farePathResponse = fareMapService.findPathWithFare(1L, 3L, PathType.DISTANCE);

        // then
        assertAll(
            () -> assertThat(farePathResponse.getStations()).isNotEmpty(),
            () -> assertThat(farePathResponse.getDuration()).isNotZero(),
            () -> assertThat(farePathResponse.getDistance()).isNotZero(),
            () -> assertThat(farePathResponse.getFare()).isNotZero()
        );
    }

    @DisplayName("전체 지하철 호선의 지도를 반환한다.")
    @Test
    void 전체_지하철_호선의_지도를_반환한다() {
        // given
        when(lineService.findLines()).thenReturn(lines);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);

        // when
        MapResponse mapResponse = fareMapService.findSubwayMap();

        // then
        assertThat(mapResponse.getLineResponses()).hasSize(3);
    }

    @DisplayName("최단 거리 기준으로 요금을 책정한다.")
    @Test
    void 거리비례제_기준으로_요금을_책정한다() {
        // given
        when(lineService.findLines()).thenReturn(lines);
        when(pathService.findPath(anyList(), anyLong(), anyLong(), any(PathType.class))).thenReturn(subwayPath, shortestPath);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);
        when(fareCalculator.calculate(shortestPath.calculateDistance())).thenReturn(FareCalculator.BASIC_FARE);

        // when
        FarePathResponse farePathResponse = fareMapService.findPathWithFare(교대역.getId(), 양재역.getId(), PathType.DURATION);

        // then
        assertAll(
            () -> assertThat(farePathResponse.getStations()).isNotEmpty(),
            () -> assertThat(farePathResponse.getDuration()).isNotZero(),
            () -> assertThat(farePathResponse.getDistance()).isNotZero(),
            () -> assertThat(farePathResponse.getFare()).isNotZero()
        );
    }
}
