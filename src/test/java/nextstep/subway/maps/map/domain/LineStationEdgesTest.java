package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineStationEdgesTest {

    private LineStationEdges lineStationEdges;

    @BeforeEach
    void setUp() {
        LineStation sourceLineStation = new LineStation(1L, null, 10, 10);
        LineStation lineStation1 = new LineStation(2L, 1L, 10, 10);
        LineStation lineStation2 = new LineStation(3L, 2L, 10, 10);
        LineStation targetLineStation = new LineStation(4L, 3L, 10, 10);
        Line line = new Line();

        lineStationEdges = new LineStationEdges(
                new LineStationEdge(sourceLineStation, line),
                new LineStationEdge(targetLineStation, line)
        );
    }

    @DisplayName("출발역을 찾는 메소드 테스트")
    @Test
    void getSourceTest() {
        // when
        Long source = lineStationEdges.getSourceStationId();

        // then
        assertThat(source).isEqualTo(1L);
    }

    @DisplayName("도착역을 찾는 메소드 테스트")
    @Test
    void getTargetTest() {
        // when
        Long source = lineStationEdges.getTargetStationId();

        // then
        assertThat(source).isEqualTo(4L);
    }
}