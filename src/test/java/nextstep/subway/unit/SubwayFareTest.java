package nextstep.subway.unit;

import nextstep.marker.ClassicUnitTest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayFare;
import nextstep.subway.domain.enums.PathType;
import nextstep.subway.domain.vo.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
    }

    @ParameterizedTest
    @CsvSource({"1, 1250", "9, 1250", "10, 1250", "11, 1350"})
    void 경로가_10km_이하이면_기본_요금을_반환한다(long distance, int expectedFare) {
        // given
        secondaryLine = getLine("2호선", "bg-red-600", distance, 40, gangnameStation, eonjuStation, 1L);
        PathFinder pathFinder = new PathFinder(secondaryLine.getSections());

        // when
        Path shortestPath = pathFinder.getShortestPath(gangnameStation, eonjuStation, PathType.DISTANCE);
        int fare = SubwayFare.calculateFare(shortestPath);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource({"10, 1250", "11, 1350", "14, 1350", "15, 1350", "49, 2050", "50, 2050", "51, 2150"})
    void 경로가_10km_초과_50km_이하면_5km당_100원씩_추가_요금이_붙는다(long distance, int expectedFare) {
        // given
        secondaryLine = getLine("2호선", "bg-red-600", distance, 40, gangnameStation, eonjuStation, 1L);
        PathFinder pathFinder = new PathFinder(secondaryLine.getSections());

        // when
        Path shortestPath = pathFinder.getShortestPath(gangnameStation, eonjuStation, PathType.DISTANCE);
        int fare = SubwayFare.calculateFare(shortestPath);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource({"49, 2050", "50, 2050", "51, 2150", "58, 2150", "59, 2250", "66, 2250", "67, 2350"})
    void 경로가_50km_초과면_8km당_100원씩_추가_요금이_붙는다(long distance, int expectedFare) {
        // given
        secondaryLine = getLine("2호선", "bg-red-600", distance, 220, gangnameStation, eonjuStation, 1L);
        PathFinder pathFinder = new PathFinder(secondaryLine.getSections());

        // when
        Path shortestPath = pathFinder.getShortestPath(gangnameStation, eonjuStation, PathType.DISTANCE);
        int fare = SubwayFare.calculateFare(shortestPath);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
