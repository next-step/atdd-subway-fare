package nextstep.subway.maps.map.application;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BidirectionalPathServiceTest {
    private List<Line> lines;
    private BidirectionalPathService pathService;

    @BeforeEach
    void setUp() {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0, 10);
        line1.addLineStation(new LineStation(1L, null, 0, 0));
        line1.addLineStation(new LineStation(2L, 1L, 2, 2));

        Line line2 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0, 10);
        line2.addLineStation(new LineStation(2L, null, 0, 0));
        line2.addLineStation(new LineStation(3L, 2L, 2, 1));

        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0, 10);
        line3.addLineStation(new LineStation(1L, null, 0, 0));
        line3.addLineStation(new LineStation(4L, 1L, 1, 2));
        line3.addLineStation(new LineStation(3L, 4L, 2, 2));

        lines = Lists.newArrayList(line1, line2, line3);

        pathService = new BidirectionalPathService();
    }

    @ParameterizedTest
    @MethodSource("findPathSourceProvider")
    void findPath(Long source, Long target, PathType type, List<Long> expectedPath) {
        //when
        SubwayPath path = pathService.findPath(lines, source, target, type, null);

        //then
        assertThat(path.extractStationId())
                .containsExactlyElementsOf(expectedPath);
    }

    private static Stream<Arguments> findPathSourceProvider() {
        return Stream.of(
                Arguments.of(1L, 3L, PathType.DISTANCE, Lists.newArrayList(1L, 4L, 3L)),
                Arguments.of(1L, 3L, PathType.DURATION, Lists.newArrayList(1L, 2L, 3L)),
                Arguments.of(3L, 1L, PathType.DURATION, Lists.newArrayList(3L, 2L, 1L))
        );
    }

    @ParameterizedTest
    @MethodSource("findPathByArrivalTimeSourceProvider")
    void findPathByArrivalTime(Long source, Long target, List<Long> expectedPath) {
        //given
        LocalDateTime departTime = LocalDateTime.of(2020, 7, 1, 6, 15);
        // when
        SubwayPath subwayPath = pathService.findPathByArrivalTime(lines, source, target, departTime);

        // then
        assertThat(subwayPath.extractStationId())
                .containsExactlyElementsOf(expectedPath);
    }

    private static Stream<Arguments> findPathByArrivalTimeSourceProvider() {
        return Stream.of(
                Arguments.of(1L, 3L, Lists.newArrayList(1L, 4L, 3L)),
                Arguments.of(3L, 1L, Lists.newArrayList(3L, 4L, 1L))
        );
    }
}