package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.application.GraphService;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathServiceTest {

    private GraphService graphService;
    private StationService stationService;
    private PathService pathService;
    private List<Line> lines;

    private SubwayGraph subwayGraph;
    private Station sourceStation;
    private Station targetStation;

    @BeforeEach
    public void setUp() {
        this.graphService = mock(GraphService.class);
        this.stationService = mock(StationService.class);
        this.pathService = new PathService(this.graphService, this.stationService);

        lines = Lists.newArrayList(new Line("이호선","green"),new Line("신분당선","red"));

        subwayGraph = new SubwayGraph(lines, PathType.DISTANCE);
        sourceStation = new Station("역삼역");
        targetStation = new Station("양재역");
    }

    @Test
    public void findPath() {
        SubwayGraph subwayGraph = new SubwayGraph(lines, PathType.DISTANCE);
        Station sourceStation = new Station("역삼역");
        Station targetStation = new Station("양재역");
        when(graphService.findGraph(any())).thenReturn(subwayGraph);
        when(stationService.findStationById(1L)).thenReturn(sourceStation);
        when(stationService.findStationById(2L)).thenReturn(targetStation);
        //when(subwayGraph.findPath(sourceStation, targetStation)).thenReturn();
    }
}
