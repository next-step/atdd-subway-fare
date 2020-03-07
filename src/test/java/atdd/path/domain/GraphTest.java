package atdd.path.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GraphTest {
    public static final List<Line> LINES = Lists.list(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);
    public static final List<Station> STATIONS = Arrays.asList(TEST_STATION_6, TEST_STATION, TEST_STATION_2);
    public static final Graph graph = new Graph(LINES);

    @Test
    public void createSubwayGraph() {
        assertThat(graph.getLines().size()).isEqualTo(4);
    }

    @Test
    public void getPath() {
        //given
        Graph graph = new Graph(LINES);

        //when
        List<Station> result = graph.getShortestDistancePath(STATION_ID, STATION_ID_3);

        //then
        assertThat(result.get(0)).isEqualTo(TEST_STATION);
        assertThat(result.get(2)).isEqualTo(TEST_STATION_3);
    }
}
