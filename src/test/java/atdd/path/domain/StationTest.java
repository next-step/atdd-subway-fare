package atdd.path.domain;

import atdd.TestConstant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationTest {

    @DisplayName("지하철역 시간표 가져오기")
    @Test
    void getTimetable() {
        // given
        Station station = TestConstant.TEST_STATION_15;
        Line line = TestConstant.TEST_LINE_4;

        // when
        List<LocalTime> timeTable = station.getTimetable(line, true);

        // then
        assertThat(timeTable.size()).isEqualTo(3);
        assertThat(timeTable).contains(LocalTime.of(10, 7, 0));
        assertThat(timeTable).contains(LocalTime.of(10, 37, 0));
        assertThat(timeTable).contains(LocalTime.of(11, 7, 0));
    }
}
