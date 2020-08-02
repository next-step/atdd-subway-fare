package nextstep.subway.maps.map.application;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.application.LineService;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.DiscountPolicy;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.map.dto.MapResponse;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.station.application.StationService;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.members.member.domain.LoginMember;
import nextstep.subway.utils.TestObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.maps.map.application.FareCalculator.BASIC_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {
    private MapService mapService;
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

    @BeforeEach
    void setUp() {
        stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 500, 10);
        LineStation lineStation1 = new LineStation(1L, null, 0, 0);
        LineStation lineStation2 = new LineStation(2L, 1L, 2, 2);
        line1.addLineStation(lineStation1);
        line1.addLineStation(lineStation2);

        Line line2 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0, 10);
        LineStation lineStation3 = new LineStation(2L, null, 0, 0);
        LineStation lineStation4 = new LineStation(3L, 2L, 2, 1);
        line2.addLineStation(lineStation3);
        line2.addLineStation(lineStation4);

        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0, 10);
        line3.addLineStation(new LineStation(1L, null, 0, 0));
        LineStation lineStation6 = new LineStation(4L, 1L, 1, 2);
        LineStation lineStation7 = new LineStation(3L, 4L, 2, 2);
        line3.addLineStation(lineStation6);
        line3.addLineStation(lineStation7);

        lines = Lists.newArrayList(line1, line2, line3);

        List<LineStationEdge> lineStations = Lists.newArrayList(
                new LineStationEdge(lineStation6, line3),
                new LineStationEdge(lineStation7, line3)
        );
        subwayPath = new SubwayPath(lineStations);
        List<LineStationEdge> shortestLineStations = Lists.newArrayList(
                new LineStationEdge(lineStation1, line1),
                new LineStationEdge(lineStation2, line1),
                new LineStationEdge(lineStation3, line2),
                new LineStationEdge(lineStation4, line2)
        );

        shortestPath = new SubwayPath(shortestLineStations);
        mapService = new MapService(lineService, stationService, pathService, fareCalculator);
    }

    @Test
    void loginUserFindPath() {
        LoginMember user = new LoginMember(1L, "email@email.com", "password", 20);
        when(lineService.findLines()).thenReturn(lines);
        when(pathService.findPath(anyList(), anyLong(), anyLong(), any())).thenReturn(subwayPath);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);
        when(fareCalculator.calculate(any(SubwayPath.class), any(DiscountPolicy.class))).thenReturn(BASIC_FARE);

        PathResponse pathResponse = mapService.findPath(user, 1L, 3L, PathType.DISTANCE, null);

        assertThat(pathResponse.getStations()).isNotEmpty();
        assertThat(pathResponse.getDuration()).isNotZero();
        assertThat(pathResponse.getDistance()).isNotZero();
        assertThat(pathResponse.getFare()).isEqualTo(BASIC_FARE.amount());
    }

    @Test
    void findPath() {
        when(lineService.findLines()).thenReturn(lines);
        when(pathService.findPath(anyList(), anyLong(), anyLong(), any())).thenReturn(subwayPath);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);
        when(fareCalculator.calculate(any(SubwayPath.class))).thenReturn(BASIC_FARE);

        PathResponse pathResponse = mapService.findPath(1L, 3L, PathType.DISTANCE, null);

        assertThat(pathResponse.getStations()).isNotEmpty();
        assertThat(pathResponse.getDuration()).isNotZero();
        assertThat(pathResponse.getDistance()).isNotZero();
        assertThat(pathResponse.getFare()).isEqualTo(BASIC_FARE.amount());
    }

    @DisplayName("최단 거리 기준으로 요금을 책정한다")
    @Test
    void calculateFareWithShortestPath() {
        when(lineService.findLines()).thenReturn(lines);
        when(pathService.findPath(anyList(), anyLong(), anyLong(), any(PathType.class))).thenReturn(shortestPath, subwayPath);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);
        when(fareCalculator.calculate(any())).thenReturn(BASIC_FARE);

        PathResponse pathResponse = mapService.findPath(1L, 3L, PathType.DURATION, null);

        assertThat(pathResponse.getStations()).isNotEmpty();
        assertThat(pathResponse.getDuration()).isNotZero();
        assertThat(pathResponse.getDistance()).isNotZero();
        assertThat(pathResponse.getFare()).isEqualTo(BASIC_FARE.amount());
    }


    @Test
    void findMap() {
        when(lineService.findLines()).thenReturn(lines);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);

        MapResponse mapResponse = mapService.findMap();

        assertThat(mapResponse.getLineResponses()).hasSize(3);
    }
}
