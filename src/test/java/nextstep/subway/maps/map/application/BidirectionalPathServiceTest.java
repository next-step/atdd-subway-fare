package nextstep.subway.maps.map.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;

class BidirectionalPathServiceTest {

    private List<Line> lines;
    private BidirectionalPathService pathService;

    private static Stream<Arguments> 경로_반환_데이터() {
        return Stream.of(
            Arguments.of(1L, 3L, PathType.DISTANCE, Lists.newArrayList(1L, 4L, 3L)),
            Arguments.of(1L, 3L, PathType.DURATION, Lists.newArrayList(1L, 2L, 3L)),
            Arguments.of(3L, 1L, PathType.DURATION, Lists.newArrayList(3L, 2L, 1L))
        );
    }

    private static Stream<Arguments> 도착시간_경로_반환_데이터() {
        return Stream.of(
            Arguments.of(1L, 3L, Lists.newArrayList(1L, 4L, 3L)),
            Arguments.of(3L, 1L, Lists.newArrayList(3L, 4L, 1L))
        );
    }

    @BeforeEach
    void setUp() {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line 서울_지하철_2호선 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0, 10);
        LineStation 지하철_2호선_교대역 = new LineStation(1L, null, 0, 0);
        LineStation 지하철_2호선_강남역 = new LineStation(2L, 1L, 2, 2);
        서울_지하철_2호선.addLineStation(지하철_2호선_교대역);
        서울_지하철_2호선.addLineStation(지하철_2호선_강남역);

        Line 신분당선 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0, 10);
        LineStation 신분당선_강남역 = new LineStation(2L, null, 0, 0);
        LineStation 신분당선_양재역 = new LineStation(3L, 2L, 2, 1);
        신분당선.addLineStation(신분당선_강남역);
        신분당선.addLineStation(신분당선_양재역);

        Line 서울_지하철_3호선 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0, 10);
        LineStation 지하철_3호선_교대역 = new LineStation(1L, null, 0, 0);
        LineStation 지하철_3호선_남부터미널역 = new LineStation(4L, 1L, 1, 2);
        LineStation 지하철_3호선_양재역 = new LineStation(3L, 4L, 2, 2);
        서울_지하철_3호선.addLineStation(지하철_3호선_교대역);
        서울_지하철_3호선.addLineStation(지하철_3호선_남부터미널역);
        서울_지하철_3호선.addLineStation(지하철_3호선_양재역);

        lines = Lists.newArrayList(서울_지하철_2호선, 서울_지하철_3호선, 신분당선);

        pathService = new BidirectionalPathService();
    }

    @DisplayName("출발지와 도착지, 검색 유형에 따라 올바르게 경로를 반환할 수 있다.")
    @MethodSource("경로_반환_데이터")
    @ParameterizedTest
    void 경로를_반환한다(Long source, Long target, PathType type, List<Long> expectedPath) {
        // when
        SubwayPath path = pathService.findPath(lines, source, target, type, null);

        //then
        assertThat(path.extractStationId())
            .containsExactlyElementsOf(expectedPath);
    }

    @DisplayName("도착 시간을 기준으로 올바르게 경로를 반환할 수 있다.")
    @MethodSource("도착시간_경로_반환_데이터")
    @ParameterizedTest
    void 도착_시간을_기준으로_경로를_반환한다(Long source, Long target, List<Long> expectedPath) {
        //given
        LocalDateTime departTime = LocalDateTime.of(2020, 7, 1, 6, 15);
        // when
        SubwayPath subwayPath = pathService.findPathByArrivalTime(lines, source, target, departTime);

        // then
        assertThat(subwayPath.extractStationId())
            .containsExactlyElementsOf(expectedPath);
    }
}
