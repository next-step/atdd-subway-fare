package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathDirectionTest {

    private LineStationEdge lineStationEdge1;
    private LineStationEdge lineStationEdge2;

    @BeforeEach
    void setUp() {
        Line line = new Line("테스트 노선", "blue", LocalTime.now(), LocalTime.now(), 0);
        LineStation lineStation1 = new LineStation(8L, 7L, 0, 0);
        LineStation lineStation2 = new LineStation(9L, 8L, 0, 0);
        lineStationEdge1 = new LineStationEdge(lineStation1, line);
        lineStationEdge2 = new LineStationEdge(lineStation2, line);
    }

    @DisplayName("정방향 노선 테스트")
    @Test
    void getForwardDirectionTest() {
        // given
        List<LineStationEdge> lineStationEdges = new ArrayList<>();
        lineStationEdges.add(lineStationEdge1);
        lineStationEdges.add(lineStationEdge2);

        // when
        PathDirection direction = PathDirection.getDirection(lineStationEdges);

        // then
        assertThat(direction).isEqualTo(PathDirection.FORWARD);
    }

    @DisplayName("정방향 노선 테스트")
    @Test
    void getReverseDirectionTest() {
        // given
        List<LineStationEdge> lineStationEdges = new ArrayList<>();
        lineStationEdges.add(lineStationEdge2);
        lineStationEdges.add(lineStationEdge1);

        // when
        PathDirection direction = PathDirection.getDirection(lineStationEdges);

        // then
        assertThat(direction).isEqualTo(PathDirection.REVERS);
    }
}