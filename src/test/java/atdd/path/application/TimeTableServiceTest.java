package atdd.path.application;

import atdd.path.dao.EdgeDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.domain.TimeTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TimeTableService.class)
public class TimeTableServiceTest {
    private TimeTableService timeTableService;

    @MockBean
    LineDao lineDao;

    @MockBean
    StationDao stationDao;

    @MockBean
    EdgeDao edgeDao;

    @BeforeEach
    void setUp() {
        this.timeTableService = new TimeTableService(lineDao, stationDao, edgeDao);
    }

    @Test
    void 노선의_지하철역_목록을_주면_지금_지하철역이_상행선일때_몇_번쨰인지_반환한다() {
        //given
        List<Station> stations
                = Arrays.asList(TEST_STATION_4, TEST_STATION, TEST_STATION_2, TEST_STATION_3);

        //when
        int index = timeTableService.calculateIndex(stations, TEST_STATION);

        //then
        assertThat(index).isEqualTo(1);
    }

    @Test
    void 노선의_지하철역_목록을_주면_지금_지하철역이_하행선일때_몇_번째인지_반환한다() {
        //given
        List<Station> stations
                = Arrays.asList(TEST_STATION_4, TEST_STATION, TEST_STATION_2, TEST_STATION_3);

        //when
        int index = timeTableService.calculateIndexReverse(stations, TEST_STATION_4);

        //then
        assertThat(index).isEqualTo(3);
    }

    @Test
    void 노선정보와_지하철역의_인덱스를_주면_지하철역에서의_첫차_시간을_반환한다() {
        //given
        int index = 2;
        Line line = new Line(1L, LINE_NAME,
                LocalTime.of(05, 00), LocalTime.of(23, 50), 10);

        //when
        LocalTime firstTime = timeTableService.calculateFirstTime(line, index);

        //then
        assertThat(firstTime).isEqualTo(line.getStartTime().plusMinutes(line.getInterval() * 2));
    }

    @Test
    void 노선정보와_지하철역의_인덱스를_주면_지하철역에서의_막차_시간을_반환한다() {
        //given
        int index = 2;
        Line line = new Line(1L, LINE_NAME,
                LocalTime.of(05, 00), LocalTime.of(20, 00), 10);

        //when
        LocalTime lastTime = timeTableService.calculateLastTime(line, index);

        //then
        assertThat(lastTime).isEqualTo(line.getEndTime().plusMinutes(line.getInterval() * 2));
    }

    @Test
    void 노선의_첫차막차_시간_노선의_배차간격을_주면_열차시간표_목록을_반환한다() {
        //given
        LocalTime firstTime = LocalTime.of(05, 00);
        LocalTime lastTime = LocalTime.of(07, 00);
        int interval = 10;
        Line line = new Line(1L, LINE_NAME,
                LocalTime.of(05, 00), LocalTime.of(20, 00), 10);

        //when
        List<LocalTime> timeTable = timeTableService.makeTimeTable(line, firstTime, lastTime, interval);

        //then
        assertThat(timeTable.get(timeTable.size() - 1)).isBeforeOrEqualTo(lastTime);
    }

    @Test
    void 노선정보와_지하철역목록과_지하철역정보를_주면_상하행선_열차시간표를_반환한다() {
        //given
        List<Station> stations
                = Arrays.asList(TEST_STATION_4, TEST_STATION, TEST_STATION_2, TEST_STATION_3);
        Line line = new Line(1L, LINE_NAME,
                LocalTime.of(05, 00), LocalTime.of(06, 00), 10);

        //when
        TimeTables timeTables = timeTableService.showTimeTablesForUpDown(line, stations, TEST_STATION);

        //then
        assertThat(timeTables.getUp()).isNotEmpty();
        assertThat(timeTables.getDown()).isNotEmpty();
        assertThat(timeTables.getUp().get(timeTables.getUp().size() - 1)).isAfter(line.getEndTime());
        assertThat(timeTables.getUp().get(timeTables.getDown().size() - 1)).isAfter(line.getEndTime());
    }
}
