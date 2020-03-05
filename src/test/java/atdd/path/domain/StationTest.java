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
        LocalTime startTime = station.calculateStartTime(LINE, index);

        //then
        assertThat(startTime)
                .isEqualTo(LINE.getStartTime().plusMinutes(LINE.getInterval()));
    }

    @Test
    void calculateLastTime() {
        //given
        Station station = TEST_STATION_2;
        int index = STATIONS.indexOf(station);

        //when
        LocalTime endTime = station.calculateEndTime(LINE, index);

        //then
        assertThat(endTime).isEqualTo(LINE.getEndTime().plusMinutes(LINE.getInterval()));
    }

    @Test
    void getTimeTables() {
        //given
        Station station = TEST_STATION_2;
        int index = STATIONS.indexOf(station);
        int indexReverse = STATIONS.size() - STATIONS.indexOf(station) - 1;
        LocalTime startTimeOfLine = LINE.getStartTime();
        LocalTime endTimeOfLine = LINE.getEndTime();
        int lineInterval = LINE.getInterval();

        //when
        TimeTables timeTablesOfStation = station.showTimeTablesForUpDown(LINE, STATIONS);

        //then
        int lastIndexForUp = timeTablesOfStation.getUp().size() - 1;
        int lastIndexForDown = timeTablesOfStation.getDown().size() - 1;
        assertThat(timeTablesOfStation.getUp().get(0))
                .isEqualTo(startTimeOfLine.plusMinutes(lineInterval));
        assertThat(timeTablesOfStation.getUp().get(lastIndexForUp))
                .isEqualTo(endTimeOfLine.plusMinutes(lineInterval*index));
        assertThat(timeTablesOfStation.getDown().get(lastIndexForDown))
                .isEqualTo(endTimeOfLine.plusMinutes(lineInterval*indexReverse));

    }
}
