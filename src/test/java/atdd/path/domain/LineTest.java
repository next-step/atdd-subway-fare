package atdd.path.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    public static final Line LINE
            = new Line(1L, "LINE_2",
            LocalTime.of(05, 00),
            LocalTime.of(07, 00), 10);

    @Test
    void 노선의_시간표_구하기() {
        //given
        Line line = LINE;

        //when
        List<LocalTime> timeTable
                = line.makeTimeTable(line.getStartTime(), line.getEndTime());
        int lastIndexOfTimeTable = timeTable.size() - 1;

        //then
        assertThat(timeTable.get(0))
                .isEqualTo(LocalTime.of(05, 00));
        assertThat(timeTable.get(lastIndexOfTimeTable))
                .isEqualTo(LocalTime.of(07, 00));
    }
}
