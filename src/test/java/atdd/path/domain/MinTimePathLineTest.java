package atdd.path.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.*;
import static atdd.TestConstant.TEST_STATION_4;
import static org.assertj.core.api.Assertions.assertThat;

public class MinTimePathLineTest {
    @Test
    public void listOf() {
        List<Line> lines = Arrays.asList(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);
        List<Station> pathStations = Arrays.asList(TEST_STATION_11, TEST_STATION_12, TEST_STATION, TEST_STATION_2, TEST_STATION_3, TEST_STATION_4);

        List<MinTimePathLine> minTimePathLines = MinTimePathLine.listOf(lines, pathStations);

        assertThat(minTimePathLines.size()).isEqualTo(2);
        assertThat(minTimePathLines.get(0).getLine().getName()).isEqualTo(LINE_NAME_3);
        assertThat(minTimePathLines.get(0).getEdges().size()).isEqualTo(1);
        assertThat(minTimePathLines.get(0).getEdges().get(0).getId()).isEqualTo(EDGE_ID_10);

        assertThat(minTimePathLines.get(1).getLine().getName()).isEqualTo(LINE_NAME);
        assertThat(minTimePathLines.get(1).getEdges().size()).isEqualTo(4);
        assertThat(minTimePathLines.get(1).getEdges().get(0).getId()).isEqualTo(EDGE_ID_23);
        assertThat(minTimePathLines.get(1).getEdges().get(1).getId()).isEqualTo(EDGE_ID);
        assertThat(minTimePathLines.get(1).getEdges().get(2).getId()).isEqualTo(EDGE_ID_2);
        assertThat(minTimePathLines.get(1).getEdges().get(3).getId()).isEqualTo(EDGE_ID_3);
    }
}
