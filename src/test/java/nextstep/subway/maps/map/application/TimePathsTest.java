package nextstep.subway.maps.map.application;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.*;
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
    private SubwayPath subwayPath1;
    private SubwayPath subwayPath2;

    @BeforeEach
    void setUp() {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, TestObjectUtils.createStation(1L, "교대역"));
        stations.put(2L, TestObjectUtils.createStation(2L, "강남역"));
        stations.put(3L, TestObjectUtils.createStation(3L, "양재역"));
        stations.put(4L, TestObjectUtils.createStation(4L, "남부터미널역"));

        Line line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN", 0);
        LineStation lineStation1 = new LineStation(1L, null, 0, 0);
        LineStation lineStation2 = new LineStation(2L, 1L, 2, 2);
        line1.addLineStation(lineStation1);
        line1.addLineStation(lineStation2);

        Line line2 = TestObjectUtils.createLine(2L, "신분당선", "RED", 0);
        LineStation lineStation3 = new LineStation(2L, null, 0, 0);
        LineStation lineStation4 = new LineStation(3L, 2L, 2, 1);
        line2.addLineStation(lineStation3);
        line2.addLineStation(lineStation4);

        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0);
        LineStation lineStation5 = new LineStation(1L, null, 0, 0);
        LineStation lineStation6 = new LineStation(4L, 1L, 1, 2);
        LineStation lineStation7 = new LineStation(3L, 4L, 2, 2);
        line3.addLineStation(lineStation5);
        line3.addLineStation(lineStation6);
        line3.addLineStation(lineStation7);

        LineStationEdge lineStationEdge1 = new LineStationEdge(lineStation1, line1);
        LineStationEdge lineStationEdge2 = new LineStationEdge(lineStation2, line1);
        LineStationEdge lineStationEdge3 = new LineStationEdge(lineStation3, line2);
        LineStationEdge lineStationEdge4 = new LineStationEdge(lineStation4, line2);
        subwayPath1 = new SubwayPath(Lists.newArrayList(lineStationEdge1, lineStationEdge2, lineStationEdge3, lineStationEdge4));

        LineStationEdge lineStationEdge5 = new LineStationEdge(lineStation5, line3);
        LineStationEdge lineStationEdge6 = new LineStationEdge(lineStation6, line3);
        LineStationEdge lineStationEdge7 = new LineStationEdge(lineStation7, line3);
        subwayPath2 = new SubwayPath(Lists.newArrayList(lineStationEdge5, lineStationEdge6, lineStationEdge7));
    }

    @Test
    void findFastestArrivalPath() {
        //given
        TimePaths timePaths = TimePaths.of(Lists.newArrayList(subwayPath1, subwayPath2));

        //when
        TimePath fastestArrivalPath = timePaths.findFastestArrivalPath(LocalDateTime.of(2020, 7, 22, 6, 15));

        //then
        assertThat(fastestArrivalPath.getPath().extractStationId())
                .containsExactly(1L, 4L, 3L);
    }
}