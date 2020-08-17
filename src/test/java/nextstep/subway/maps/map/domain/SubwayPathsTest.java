package nextstep.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.application.PathService;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;

public class SubwayPathsTest {

    private List<Line> lines;
    private KShortestPaths<Long, LineStationEdge> kShortestPaths;
    private SubwayPath subwayPath1;
    private SubwayPath subwayPath2;

    @BeforeEach
    void setUp() {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line 서울_지하철_2호선 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0, 3);
        LineStation 서울_지하철_2호선_교대역 = new LineStation(1L, null, 0, 0);
        LineStation 서울_지하철_2호선_강남역 = new LineStation(2L, 1L, 2, 2);
        서울_지하철_2호선.addLineStation(서울_지하철_2호선_교대역);
        서울_지하철_2호선.addLineStation(서울_지하철_2호선_강남역);

        Line 신분당선 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0, 10);
        LineStation 신분당선_강남역 = new LineStation(2L, null, 0, 0);
        LineStation 신분당선_양재역 = new LineStation(3L, 2L, 2, 1);
        신분당선.addLineStation(신분당선_강남역);
        신분당선.addLineStation(신분당선_양재역);

        Line 서울_지하철_3호선 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0, 5);
        LineStation 서울_지하철_3호선_교대역 = new LineStation(1L, null, 0, 0);
        LineStation 서울_지하철_3호선_남부터미널역 = new LineStation(4L, 1L, 1, 2);
        LineStation 서울_지하철_3호선_양재역 = new LineStation(3L, 4L, 2, 2);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_교대역);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_남부터미널역);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_양재역);

        LineStationEdge 서울_지하철_2호선_강남역_에지 = new LineStationEdge(서울_지하철_2호선_강남역, 서울_지하철_2호선);
        LineStationEdge 신분당선_양재역_에지 = new LineStationEdge(신분당선_양재역, 신분당선);

        subwayPath1 = new SubwayPath(Lists.newArrayList(
            서울_지하철_2호선_강남역_에지, 신분당선_양재역_에지),
            1L
        );

        LineStationEdge 서울_지하철_3호선_남부터미널역_에지 = new LineStationEdge(서울_지하철_3호선_남부터미널역, 서울_지하철_3호선);
        LineStationEdge 서울_지하철_3호선_양재역_에지 = new LineStationEdge(서울_지하철_3호선_양재역, 서울_지하철_3호선);

        subwayPath2 = new SubwayPath(Lists.newArrayList(
            서울_지하철_3호선_남부터미널역_에지,
            서울_지하철_3호선_양재역_에지),
            1L
        );

        SubwayGraph graph = new SubwayGraph(LineStationEdge.class);
        lines = Lists.newArrayList(서울_지하철_2호선, 신분당선, 서울_지하철_3호선);
        graph.addVertexWith(lines);
        graph.addEdge(lines, PathType.DURATION);

        kShortestPaths = new KShortestPaths<>(graph, PathService.MAX_PATH_COUNT);
    }

    @DisplayName("현재 시간으로부터 가장 빠른 도착 경로를 반환한다.")
    @Test
    void 최단_도착_경로를_반환한다() {
        // given
        SubwayPaths subwayPaths = SubwayPaths.of(Lists.newArrayList(subwayPath1, subwayPath2));

        // when
        SubwayPath fastestArrivalPath = subwayPaths.findFastestArrivalPath(
            LocalDateTime.of(2020, 8, 24, 14, 0)
        );

        // then
        assertThat(fastestArrivalPath.extractStationId()).containsExactly(1L, 4L, 3L);
    }
}
