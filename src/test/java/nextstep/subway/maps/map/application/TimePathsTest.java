package nextstep.subway.maps.map.application;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayGraph;
import nextstep.subway.maps.map.domain.TimePath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.maps.map.application.NewTestPathService.MAX_PATH_COUNT;
import static org.assertj.core.api.Assertions.assertThat;

class TimePathsTest {

    private KShortestPaths<Long, LineStationEdge> kShortestPaths;

    @BeforeEach
    void setUp() {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0);
        line1.addLineStation(new LineStation(1L, null, 0, 0));
        line1.addLineStation(new LineStation(2L, 1L, 2, 2));

        Line line2 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0);
        line2.addLineStation(new LineStation(2L, null, 0, 0));
        line2.addLineStation(new LineStation(3L, 2L, 2, 1));

        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0);
        line3.addLineStation(new LineStation(1L, null, 0, 0));
        line3.addLineStation(new LineStation(4L, 1L, 1, 2));
        line3.addLineStation(new LineStation(3L, 4L, 2, 2));

        List<Line> lines = Lists.newArrayList(line1, line2, line3);

        SubwayGraph graph = new SubwayGraph(LineStationEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines, PathType.DURATION);

        kShortestPaths = new KShortestPaths<>(graph, MAX_PATH_COUNT);
    }

    @Test
    void findFastestArrivalPath() {
        //given
        List<GraphPath<Long, LineStationEdge>> paths = kShortestPaths.getPaths(1L, 3L);
        TimePaths timePaths = TimePaths.of(paths);

        //when
        TimePath fastestArrivalPath = timePaths.findFastestArrivalPath(LocalDateTime.of(2020, 7, 22, 6, 15));

        //then
        assertThat(fastestArrivalPath.getPath().getVertexList())
                .containsExactly(1L, 4L, 3L);
    }
}