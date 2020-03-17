package atdd.path.domain;

import org.junit.jupiter.api.Test;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class EdgesTest {
    @Test
    public void calculateDownDelayTimeOf() {
        Edges edges = new Edges(TEST_LINE.getEdges());

        int delayTime = edges.calculateDownDelayTimeOf(TEST_STATION_3.getId());

        assertThat(delayTime).isEqualTo(10);
    }

    @Test
    public void calculateUpDelayTimeOf() {
        Edges edges = new Edges(TEST_LINE.getEdges());

        int delayTime = edges.calculateUpDelayTimeOf(TEST_STATION_4.getId());

        assertThat(delayTime).isEqualTo(5);
    }
}
