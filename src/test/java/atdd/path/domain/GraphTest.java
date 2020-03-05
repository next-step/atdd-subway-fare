package atdd.path.domain;

import atdd.path.application.dto.MinTimePathResponseView;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GraphTest {
    public static final List<Line> LINES = Lists.list(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);

    @Test
    public void createSubwayGraph() {
        Graph graph = new Graph(LINES);

        assertThat(graph.getLines().size()).isEqualTo(4);
    }

    @Test
    public void getPath() {
        Graph graph = new Graph(LINES);

        List<Station> result = graph.getShortestDistancePath(STATION_ID, STATION_ID_3);

        assertThat(result.get(0)).isEqualTo(TEST_STATION);
        assertThat(result.get(2)).isEqualTo(TEST_STATION_3);
    }

    @Test
    public void getMinTimePath() {
        //given
        Graph graph = new Graph(LINES);

        //when
        MinTimePathResponseView response
                = graph.getMinTimePath(STATION_ID_5, STATION_ID_16);//from 종합운동장 to 대치

        //then
        assertThat(response.getStartStationId()).isEqualTo(5L);
        assertThat(response.getEndStationId()).isEqualTo(16L);
        assertThat(response.getLines().size()).isEqualTo(3);
        assertThat(response.getStations().size()).isEqualTo(6); // 출발역 포함
        assertThat(response.getDepartAt()).isAfter(LocalTime.now());
        assertThat(response.getDistance()).isEqualTo(50);
    }
}
