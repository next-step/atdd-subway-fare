package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.application.GraphService;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathServiceTest {

    private GraphService graphService;
    private StationService stationService;
    private PathService pathService;
    private List<Line> lines;

    private SubwayGraph subwayGraph;
    private Station 역삼역;
    private Station 양재역;
    private Station 강남역;
    private Station 서초역;

    private Line 이호선;
    private Line 신분당선;

    private final static int DEFAULT_FARE = 1_250;

    @BeforeEach
    public void setUp() {
        this.graphService = mock(GraphService.class);
        this.stationService = mock(StationService.class);
        this.pathService = new PathService(this.graphService, this.stationService);

        역삼역 = new Station("역삼역");
        양재역 = new Station("양재역");
        강남역 = new Station("강남역");
        서초역 = new Station("서초역");

        이호선 = new Line("이호선", "green");
        신분당선 = new Line("신분당선", "red");
        이호선.addSection(역삼역, 양재역, 9, 10);
        이호선.addSection(양재역, 강남역, 3, 5);
        이호선.addSection(강남역, 서초역, 50, 5);

        lines = Lists.newArrayList(이호선, 신분당선);
        subwayGraph = new SubwayGraph(lines, PathType.DISTANCE);
    }

    @Test
    public void findPath() {
        SubwayGraph subwayGraph = new SubwayGraph(lines, PathType.DISTANCE);
        Station sourceStation = 역삼역;
        Station targetStation = 양재역;
        when(graphService.findGraph(any())).thenReturn(subwayGraph);
        when(stationService.findStationById(1L)).thenReturn(sourceStation);
        when(stationService.findStationById(2L)).thenReturn(targetStation);

        //then
        PathResponse response = pathService.findPath(1L, 2L, PathType.DISTANCE);
        assertThat(response.getDistance()).isEqualTo(9);
        assertThat(response.getFare()).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("거리가 10 이하일때 기본 요금임을 확인")
    @Test
    public void defaultFare() {
        int fare = pathService.calculateFareWithDistance(10);

        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("거리가 10 초과 50 이하 일때 요금 확인")
    @Test
    public void over10KmFare() {
        int fare = pathService.calculateFareWithDistance(12);

        assertThat(fare).isEqualTo(DEFAULT_FARE + 100);
    }

    @DisplayName("거리가 10 초과 50 이하 일때 요금 확인2")
    @Test
    public void over10KmFare2() {
        int fare = pathService.calculateFareWithDistance(16);

        assertThat(fare).isEqualTo(DEFAULT_FARE + 200);
    }

    @DisplayName("거리가 50 초과 일때 요금 확인")
    @Test
    public void over50KmFare() {
        int fare = pathService.calculateFareWithDistance(51);

        assertThat(fare).isEqualTo(DEFAULT_FARE + 600);
    }

    @DisplayName("거리가 10이하인데 추가요금 부과 확인")
    @Test
    public void defaultFareWithAdditionalFee() {
        int fare = pathService.calculateFareWithDistance(10);

        //assertThat(fare)
    }

}
