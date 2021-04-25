package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.application.FareService;
import nextstep.subway.path.application.GraphService;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathServiceTest {

    private GraphService graphService;
    private StationService stationService;
    private PathService pathService;
    private FareService fareService;
    private Sections sections;
    private List<Line> lines;

    private SubwayGraph subwayGraph;
    private Station 역삼역;
    private Station 양재역;
    private Station 강남역;
    private Station 서초역;

    private Line 이호선;
    private Line 신분당선;

    private LoginMember loginMember;

    private final static int DEFAULT_FARE = 1_250;

    private Fare fare;

    @BeforeEach
    public void setUp() {
        this.graphService = mock(GraphService.class);
        this.stationService = mock(StationService.class);
        this.fareService = mock(FareService.class);
        this.sections = mock(Sections.class);
        this.pathService = new PathService(this.graphService, this.stationService, this.fareService);

        역삼역 = new Station("역삼역");
        양재역 = new Station("양재역");
        강남역 = new Station("강남역");
        서초역 = new Station("서초역");

        이호선 = new Line("이호선", "green", 0);
        신분당선 = new Line("신분당선", "red", 1_000);

        이호선.addSection(역삼역, 양재역, 9, 10);
        이호선.addSection(양재역, 강남역, 3, 5);
        이호선.addSection(강남역, 서초역, 50, 5);

        lines = Lists.newArrayList(이호선, 신분당선);
        subwayGraph = new SubwayGraph(lines, PathType.DISTANCE);

        loginMember = new LoginMember(1L, "gpwls", "1234", 100);

        fare = Fare.createInstance(10,0);
    }

    @Test
    public void findPath() {
        SubwayGraph subwayGraph = new SubwayGraph(lines, PathType.DISTANCE);
        Station sourceStation = 역삼역;
        Station targetStation = 양재역;
        when(graphService.findGraph(any())).thenReturn(subwayGraph);
        when(stationService.findStationById(1L)).thenReturn(sourceStation);
        when(stationService.findStationById(2L)).thenReturn(targetStation);
        when(stationService.findStationById(2L)).thenReturn(targetStation);
        when(sections.getTotalDistance()).thenReturn(10);
        when(fareService.calculate(subwayGraph.findPath(sourceStation, targetStation).getTotalDistance(), 0, loginMember)).thenReturn(fare);

        //then
        PathResponse response = pathService.findPath(1L, 2L, PathType.DISTANCE, loginMember);
        assertThat(response.getDistance()).isEqualTo(9);
        assertThat(response.getFare()).isEqualTo(DEFAULT_FARE);
    }
}
