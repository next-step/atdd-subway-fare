package atdd.path.domain;

import atdd.TestConstant;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EdgesTest {

    @DisplayName("종점에서 목표 역까지 걸리는 시간을 조회")
    @Test
    void getElapsedTimeBy() {
        // given
        Edges edges = new Edges(Lists.list(
                TestConstant.TEST_EDGE_16,
                TestConstant.TEST_EDGE_17,
                TestConstant.TEST_EDGE_18,
                TestConstant.TEST_EDGE_19,
                TestConstant.TEST_EDGE_20,
                TestConstant.TEST_EDGE_21,
                TestConstant.TEST_EDGE_22));

        // when
        Long upTime = edges.getElapsedTimeBy(TestConstant.STATION_ID_17, true);
        Long downTime = edges.getElapsedTimeBy(TestConstant.STATION_ID_17, false);

        // then
        assertThat(upTime).isEqualTo(0);
        assertThat(downTime).isEqualTo(12);
    }

    @DisplayName("종점에서 목표 역까지 걸리는 시간을 조회_구간에 없는 역")
    @Test
    void getElapsedTimeBy_findInvalidStationId() {
        Edges edges = new Edges(Lists.list(
                TestConstant.TEST_EDGE_16,
                TestConstant.TEST_EDGE_17,
                TestConstant.TEST_EDGE_18,
                TestConstant.TEST_EDGE_19,
                TestConstant.TEST_EDGE_20,
                TestConstant.TEST_EDGE_21,
                TestConstant.TEST_EDGE_22));

        assertThrows(IllegalArgumentException.class, () -> edges.getElapsedTimeBy(TestConstant.STATION_ID_9, true));
        assertThrows(IllegalArgumentException.class, () -> edges.getElapsedTimeBy(TestConstant.STATION_ID_9, false));
    }
}
