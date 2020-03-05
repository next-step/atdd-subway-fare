package atdd.path.domain;

import atdd.path.application.dto.MinTimePathResponseView;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GraphTest {
    public static final List<Line> LINES = Lists.list(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);
    public static final List<Station> STATIONS = Arrays.asList(TEST_STATION_6, TEST_STATION, TEST_STATION_2);
    public static final Graph graph = new Graph(LINES);

    @Test
    public void createSubwayGraph() {
//        Graph graph = new Graph(LINES);

        assertThat(graph.getLines().size()).isEqualTo(4);
    }

    @Test
    public void getPath() {
        //given
        Graph graph = new Graph(LINES);

        //when
        List<Station> result = graph.getShortestDistancePath(STATION_ID, STATION_ID_3);

        //then
        assertThat(result.get(0)).isEqualTo(TEST_STATION);
        assertThat(result.get(2)).isEqualTo(TEST_STATION_3);
    }


    @Test
    public void findLineToStart() {
        //given
        List<Line> lines = LINES;
        List<Station> stations = STATIONS;

        //when
        Line lineToStart = graph.findLineToStart(lines, stations);

        //then
        assertThat(lineToStart.getName()).isEqualTo("신분당선");
    }

    @Test
    public void findLinesForPath() {
        //given
        List<Line> lines = LINES;
        List<Station> stations = STATIONS;

        //when
        Set<Line> linesForPath = graph.findLinesForPath(lines, stations);

        //then
        assertThat(linesForPath.size()).isEqualTo(2);
    }

    @Test
    public void findLineToStartTest() {
        //given
        List<Line> lines = LINES;
        List<Station> stations = STATIONS;

        //when
        Line lineToStart = graph.findLineToStart(lines, stations);

        //then
        assertThat(lineToStart.getName()).isEqualTo("신분당선");
    }

    @Test
    public void calculateTimeToArriveTest() {
        //given
        List<Line> lines = LINES;
        List<Station> stations = STATIONS;
        LocalTime departBy = LocalTime.of(07, 30);
        double timeToTake_min = 60;

        //when
        LocalTime arriveAt = graph.calculateTimeToArrive(departBy, timeToTake_min);

        //then
        assertThat(arriveAt).isEqualTo(LocalTime.of(8, 30));
    }

    @Test
    public void getMinTimePathTest() {
        //given
        Graph graph = new Graph(LINES);

        //when
        MinTimePathResponseView response
                = graph.getMinTimePath(STATION_ID_5, STATION_ID_16);//from 종합운동장 to 대치

        //then
        assertThat(response.getStartStationId()).isEqualTo(5L);
        assertThat(response.getEndStationId()).isEqualTo(16L);
        assertThat(response.getLines().size()).isEqualTo(3);
        assertThat(response.getStations().size()).isEqualTo(6); // 출발역 포함
        assertThat(response.getDepartAt()).isAfter(LocalTime.now());
        assertThat(response.getDistance()).isEqualTo(50);
    }
}
