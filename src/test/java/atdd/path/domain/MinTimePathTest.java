package atdd.path.domain;

import atdd.path.application.dto.MinTimePathResponseView;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MinTimePathTest {
    public static final List<Line> LINES = Lists.list(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);
    public static final Graph graph = new Graph(LINES);

    @Test
    public void findMinTimeResponseViewTest() {
        //when
        MinTimePathResponseView responseView = graph.getMinTimePath(STATION_ID_6, STATION_ID_2);

        //then
        assertThat(responseView.getStartStationId()).isEqualTo(STATION_ID_6);
        assertThat(responseView.getEndStationId()).isEqualTo(STATION_ID_2);
        assertThat(responseView.getDistance()).isEqualTo(20);
        assertThat(responseView.getStations().size()).isEqualTo(3);
        assertThat(responseView.getLines().contains(TEST_LINE)).isTrue();
        assertThat(responseView.getLines().contains(TEST_LINE_2)).isTrue();
        assertThat(responseView.getLines().size()).isEqualTo(2);
    }
}
