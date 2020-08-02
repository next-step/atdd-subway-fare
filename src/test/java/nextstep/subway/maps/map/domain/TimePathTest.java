package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.utils.TestObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TimePathTest {

    private TimePath timePath;

    @BeforeEach
    void setUp() {
        Line line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0, 5);
        LineStation lineStation1 = new LineStation(1L, null, 0, 0);
        LineStation lineStation2 = new LineStation(4L, 1L, 1, 2);
        LineStation lineStation3 = new LineStation(3L, 4L, 2, 2);
        line3.addLineStation(lineStation1);
        line3.addLineStation(lineStation2);
        line3.addLineStation(lineStation3);

        LineStationEdge lineStationEdge1 = new LineStationEdge(lineStation1, line3);
        LineStationEdge lineStationEdge2 = new LineStationEdge(lineStation2, line3);
        LineStationEdge lineStationEdge3 = new LineStationEdge(lineStation3, line3);
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(lineStationEdge1, lineStationEdge2, lineStationEdge3);
        SubwayPath subwayPath = new SubwayPath(lineStationEdges);
        timePath = new TimePath(subwayPath);
    }

    @Test
    void getArrivalTime() {
        //when
        LocalDateTime arrivalTime = timePath.getArrivalTime(LocalDateTime.of(2020, 7, 22, 6, 15));

        //then
        assertThat(arrivalTime)
                .isEqualTo(LocalDateTime.of(2020, 7, 22, 6, 19));
    }
}