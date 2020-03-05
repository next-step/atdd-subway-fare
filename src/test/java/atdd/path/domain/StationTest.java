package atdd.path.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StationTest {
    public static final Line LINE
            = new Line(1L, "LINE_2",
            LocalTime.of(05, 00),
            LocalTime.of(07, 00), 10);

    public static final List<Station> STATIONS
            = Arrays.asList(TEST_STATION, TEST_STATION_2, TEST_STATION_3, TEST_STATION_4);

    @Test
    void calculateFirstTime() {
        //given
        Station station = TEST_STATION_2;
        int index = STATIONS.indexOf(station);

        //when
        assertThat(station.calculateFirstTime(LINE, index))
                .isEqualTo(LINE.getStartTime().plusMinutes(LINE.getInterval()));
    }
}
