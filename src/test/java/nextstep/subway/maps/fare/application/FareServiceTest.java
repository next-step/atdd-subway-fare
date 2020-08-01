package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.PathService;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        Fare fare = fareService.calculateFare(lines, source, target, distance, type);

        // then
        assertThat(fare.getValue()).isNotNegative();
        verify(fareCalculator).calculate(any());

    }

    @Test
    public void calculateFareTestNew() {
        // given
        FareCalculator fareCalculator = mock(FareCalculator.class);
        PathService pathService = mock(PathService.class);
        FareService fareService = new FareService(pathService, fareCalculator);
        List<Line> lines = new ArrayList<>();
        List<LineStationEdge> lineStationEdges = new ArrayList<>();
        PathType type = PathType.DISTANCE;

        // when
        // List<Line> lines, Long source, Long target, SubwayPath subwayPath, PathType type
        Fare fare = fareService.calculateFare(lines, lineStationEdges, type);

        // then
        assertThat(fare.getValue()).isNotNegative();
        verify(fareCalculator).calculate(any());

    }
}