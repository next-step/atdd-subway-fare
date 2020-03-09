package atdd.path.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static atdd.TestConstant.STATION_ID_7;
import static atdd.TestConstant.TEST_LINE_2;
import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    @Test
    public void getUpTimetablesOf() {
        Line line = TEST_LINE_2;

        List<String> upTime = line.getUpTimetablesOf(STATION_ID_7);

        assertThat(upTime.size()).isEqualTo(113);
        assertThat(upTime.get(0)).isEqualTo(LocalTime.of(5, 25).toString());
        assertThat(upTime.get(upTime.size() - 1)).isEqualTo(LocalTime.of(00, 5).toString());
    }

    @Test
    public void getDownTimetablesOf() {
        Line line = TEST_LINE_2;

        List<String> downTime = line.getDownTimetablesOf(STATION_ID_7);

        assertThat(downTime.size()).isEqualTo(113);
        assertThat(downTime.get(0)).isEqualTo(LocalTime.of(5, 20).toString());
        assertThat(downTime.get(downTime.size() - 1)).isEqualTo(LocalTime.of(00, 0).toString());
    }
}
