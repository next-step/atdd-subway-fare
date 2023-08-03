package nextstep.subway.unit;

import nextstep.marker.ClassicUnitTest;
import nextstep.subway.domain.*;
import nextstep.subway.domain.enums.PathType;
import nextstep.subway.domain.vo.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.unit.utils.ClassicUnitTestUtils.getLine;
import static nextstep.subway.unit.utils.ClassicUnitTestUtils.getStation;
import static org.assertj.core.api.Assertions.assertThat;

@ClassicUnitTest
class SubwayFareTest {

    private Line secondaryLine;
    private Station gangnameStation;
    private Station eonjuStation;

    @BeforeEach
    void setUp() {
        gangnameStation = getStation("강남역", 1L);
        eonjuStation = getStation("언주역", 2L);
        secondaryLine = getLine("2호선", "bg-red-600", 10L, 40, gangnameStation, eonjuStation, 1L);
    }

    @Test
    void 경로를_받으면_요금을_반환한다() {
        // given
        PathFinder pathFinder = new PathFinder(secondaryLine.getSections());

        // when
        Path shortestPath = pathFinder.getShortestPath(gangnameStation, eonjuStation, PathType.DISTANCE);
        int fare = SubwayFare.calculateFare(shortestPath);

        // then
        assertThat(fare).isEqualTo(SubwayFare.BASIC_FARE);
    }
}
