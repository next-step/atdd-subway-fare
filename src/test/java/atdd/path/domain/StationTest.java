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
    void 지하철역의_첫차시간_구하기() {
        //given
        Station station = TEST_STATION_2;
        int index = STATIONS.indexOf(station);

        //when
        LocalTime startTime = station.calculateStartTime(LINE, index);

        //then
        assertThat(startTime)
                .isEqualTo(LocalTime.of(05, 10));
    }

    @Test
    void 지하철역의_막차시간_구하기() {
        //given
        Station station = TEST_STATION_2;
        int index = STATIONS.indexOf(station);

        //when
        LocalTime endTime = station.calculateEndTime(LINE, index);

        //then
        assertThat(endTime)
                .isEqualTo(LocalTime.of(07, 10));
    }

    @Test
    void 지하철역의_상하행선_시간표_요청하기() {
        //given
        Station station = TEST_STATION_2;

        //when
        TimeTables timeTablesOfStation = station.showTimeTablesForUpDown(LINE, STATIONS);

        //then
        int lastIndexForUp = timeTablesOfStation.getUp().size() - 1;
        int lastIndexForDown = timeTablesOfStation.getDown().size() - 1;

        assertThat(timeTablesOfStation.getUp().get(0))
                .isEqualTo(LocalTime.of(05, 10));
        assertThat(timeTablesOfStation.getUp().get(lastIndexForUp))
                .isEqualTo(LocalTime.of(07, 10));
        assertThat(timeTablesOfStation.getDown().get(lastIndexForDown))
                .isEqualTo(LocalTime.of(07, 20));
    }
}