package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.application.PathService;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FareServiceTest {

    @Test
    public void calculateFareTest() {
        // given
        FareCalculator fareCalculator = mock(FareCalculator.class);
        PathService pathService = mock(PathService.class);
        FareService fareService = new FareService(pathService, fareCalculator);
        List<Line> lines = new ArrayList<>();
        Long source = 1L;
        Long target = 2L;
        Integer distance = 10;
        PathType type = PathType.DISTANCE;

        // when
        // List<Line> lines, Long source, Long target, SubwayPath subwayPath, PathType type
        fareService.calculateFare(lines, source, target, distance, type);

        // then
        verify(fareCalculator).calculate(any());
    }

    @Test
    public void calculateFareTestNew() {
        // given
        FareCalculator fareCalculator = mock(FareCalculator.class);
        PathService pathService = mock(PathService.class);
        FareService fareService = new FareService(pathService, fareCalculator);
        LineStation lineStation = new LineStation(1L, null, 10, 10);
        Line line = TestObjectUtils.createLine(1L, "테스트", "blue");
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        List<LineStationEdge> lineStationEdges = new ArrayList<>();
        lineStationEdges.add(new LineStationEdge(lineStation, line));
        SubwayPath subwayPath = new SubwayPath(lineStationEdges);
        PathType type = PathType.DISTANCE;

        // when
        fareService.calculateFare(lines, subwayPath, type);

        // then
        verify(fareCalculator).calculate(any());
    }
}