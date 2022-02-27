package nextstep.subway.domain;

import static nextstep.subway.utils.StringDateTimeConverter.convertStringToDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

class SubwayMapTest {

    private static final String START_TIME = "202202200600";
    private static final String SHORTEST_DURATION_ARRIVAL_TIME = "202202200610";
    private static final String SHORTEST_DISTANCE_ARRIVAL_TIME = "202202200615";

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 삼성역;
    private Station 대치역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /**
     * 교대역    --- *2호선*  ---    강남역  ---  *2호선*  --- 삼성역
     *  |      거리:8, 시간:3        |       거리:8, 시간 3
     *  |                          |
     * *3호선*                   *신분당선*
     거리:6, 시간:5             거리:9, 시간:2
     *  |                          |
     *  |                          |
     * 남부터미널역  --- *3호선* ---  양재역 ---  *3호선*  ---  대치역
     *           거리:6, 시간:5             거리: 2, 시간 5
     *
     * 2호선: 추가요금 900원
     *      첫차시간 05:00
     *      막차시간 23:30
     *      운행간격 10분
     * 3호선: 추가요금 1100원
     *      첫차시간 05:30
     *      막차시간 23:00
     *      운행간격 5분
     * 신분당선: 추가요금 2000원
     *      첫차시간 05:20
     *      막차시간 23:40
     *      운행간격 20분
     *
     * 1. 강남역-양재역-남부터미널역  **최단시간 (운행시간고려 최단시간 도착) 거리 15**
     *      강남역에서 06:00 탑승
     *      양재역에서 06:02 하차
     *      양재역에서 06:05 탑승
     *      남부터미널역에서 06:10 하차 (도착) 202202200610
     *
     * 2. 강남역-교대역-남부터미널역    **최단거리 14**
     *      강남역에서 06:03 탑승
     *      교대역에서 06:06 하차
     *      교대역에서 06:10 탑승
     *      남부터미널역에서 06:15 하차 (도착) 202202200615
     *
     * 3. 남부터미널역-양재역-강남역
     *      남부터미널역에서 06:00 탑승
     *      양재역에서 06:05 하차
     *      양재역에서 06:20 탑승
     *      강남역에서 06:22 하차  (도착) 202202200622
     *
     * 4. 남부터미널역-교대역-강남역    **최단시간** **최단거리 14**
     *      남부터미널역에서 06:00 탑승
     *      교대역에서 06:05 하차
     *      교대역에서 06:10 탑승
     *      강남역에서 06:13 하차  (도착) 202202200613
     */

    private SubwayMap subwayMap = new SubwayMap();

    @BeforeEach
    void setUp() {
        createSubwayMap();

    }

    @DisplayName("KShortest 알고리즘을 사용하여 가장 최단거리 경로를 조회한다.")
    @Test
    void findShortestDistanceUsingKShortest_Test() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
//        SubwayMap subwayMap = new SubwayMap();
        subwayMap.createSubwayMapGraph(lines);
        AllKShortestPaths allPaths = subwayMap.findAllKShortestPaths(
            강남역, 남부터미널역);

        // when
        ShortestPaths shortest = allPaths.getShortestPathsFrom(START_TIME);

        // then
        assertAll(
            () -> assertThat(shortest.getShortestDistancePath()
                .getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 교대역, 남부터미널역)),
            () -> assertThat(shortest.getShortestDistance()).isEqualTo(14),
            () -> assertThat(shortest.getShortestDistanceArrivalTime()).isEqualTo(SHORTEST_DISTANCE_ARRIVAL_TIME)
        );
    }

    @DisplayName("KShortest 알고리즘을 사용하여 가장 빠른 도착시간 경로를 조회한다.")
    @Test
    void findShortestDurationUsingKShortest_Test() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
//        SubwayMap subwayMap = new SubwayMap(lines);
        subwayMap.createSubwayMapGraph(lines);
        AllKShortestPaths allPaths = subwayMap.findAllKShortestPaths(
            강남역, 남부터미널역);

        // when
        ShortestPaths shortest = allPaths.getShortestPathsFrom(START_TIME);

        // then
        assertAll(
            () -> assertThat(shortest.getShortestDurationPath()
                .getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재역, 남부터미널역)),
            () -> assertThat(shortest.getShortestDistance()).isEqualTo(14),
            () -> assertThat(shortest.getShortestDurationArrivalTime()).isEqualTo(SHORTEST_DURATION_ARRIVAL_TIME)
        );
    }

    @DisplayName("LocalDateTime 몇 분 차이가 나는지 확인한다 (양수)")
    @Test
    void minutesBetween_positive_test() {
        LocalDateTime sourceTime = convertStringToDateTime("202202221930");
        LocalDateTime targetTime = convertStringToDateTime("202202221948");

        int between = (int) ChronoUnit.MINUTES.between(sourceTime, targetTime);

        assertThat(between).isEqualTo(18);
    }

    @DisplayName("LocalDateTime 몇 분 차이가 나는지 확인한다 (음수)")
    @Test
    void minutesBetween_negative_test() {
        LocalDateTime sourceTime = convertStringToDateTime("202202221948");
        LocalDateTime targetTime = convertStringToDateTime("202202221930");

        int between = (int) ChronoUnit.MINUTES.between(sourceTime, targetTime);

        assertThat(between).isEqualTo(-18);
    }

    @DisplayName("출발시간을 고려하여 최단거리의 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
//        SubwayMap subwayMap = new SubwayMap(lines);
        subwayMap.createSubwayMapGraph(lines);

        // when
        Path path = subwayMap.findPath(강남역, 남부터미널역, PathType.DISTANCE, START_TIME);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 교대역, 남부터미널역));
    }

    @DisplayName("출발시간을 고려하여 최단거리의 경로를 조회한다. (반대방향)")
    @Test
    void findPathByDistanceOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
//        SubwayMap subwayMap = new SubwayMap(lines);
        subwayMap.createSubwayMapGraph(lines);

        // when
        Path path = subwayMap.findPath(남부터미널역, 강남역, PathType.DISTANCE, START_TIME);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(남부터미널역, 교대역, 강남역));
    }

    @DisplayName("출발시간을 고려하여 가장 빠른 도착시간의 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
//        SubwayMap subwayMap = new SubwayMap(lines);
        subwayMap.createSubwayMapGraph(lines);

        // when
        Path path = subwayMap.findPath(강남역, 남부터미널역, PathType.DURATION, START_TIME);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재역, 남부터미널역));
    }

    @DisplayName("출발시간을 고려하여 가장 빠른 도착시간의 경로를 조회한다 (반대방향).")
    @Test
    void findPathByDurationOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
//        SubwayMap subwayMap = new SubwayMap(lines);
        subwayMap.createSubwayMapGraph(lines);

        // when
        Path path = subwayMap.findPath(남부터미널역, 강남역, PathType.DURATION, START_TIME);

        // then
        assertAll(
            () -> assertThat(path.extractArrivalTime()).isEqualTo("202202200613"),
            () -> assertThat(path.getStations())
                .containsExactlyElementsOf(Lists.newArrayList(남부터미널역, 교대역, 강남역))
        );
    }

    private void createSubwayMap() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        삼성역 = createStation(5L, "삼성역");
        대치역 = createStation(6L, "대치역");

        신분당선 = Line.of("신분당선", "red", 2000
            , "0520", "2340", 20);
        이호선 = Line.of("2호선", "red", 900
            , "0500", "2330", 10);
        삼호선 = Line.of("3호선", "red", 1100
            , "0530", "2300", 5);

        신분당선.addSection(강남역, 양재역, 9, 2);
        이호선.addSection(교대역, 강남역, 8, 3);
        이호선.addSection(강남역, 삼성역, 8, 3);
        삼호선.addSection(교대역, 남부터미널역, 6, 5);
        삼호선.addSection(남부터미널역, 양재역, 6, 5);
        삼호선.addSection(양재역, 대치역, 2, 5);
    }

    private Station createStation(long id, String name) {
        Station station = Station.of(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
