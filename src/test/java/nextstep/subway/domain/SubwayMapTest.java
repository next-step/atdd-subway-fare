package nextstep.subway.domain;

import com.google.common.collect.Lists;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubwayMapTest {

    private static final String START_TIME = "202202200600";
    private static final String SHORTEST_DURATION_ARRIVAL_TIME = "202202200610";
    private static final String SHORTEST_DISTANCE_ARRIVAL_TIME = "202202200615";

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /**
     * 교대역    --- *2호선*  ---    강남역  ---  *2호선*  --- 삼성역
     *  |      거리:8, 시간:3        |       거리:8, 시간 3
     *  |                          |
     * *3호선*                   *신분당선*
     거리:6, 시간:5             거리:9, 시간:2
     |                          |
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
     * 1. 강남역-양재역-남부터미널역  **(운행시간고려 최단시간 도착) 거리 15**
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
     */
    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = Line.of("신분당선", "red");
        이호선 = Line.of("2호선", "red");
        삼호선 = Line.of("3호선", "red");

        신분당선.addSection(강남역, 양재역, 9, 2);
        이호선.addSection(교대역, 강남역, 8, 3);
        삼호선.addSection(교대역, 남부터미널역, 6, 5);
        삼호선.addSection(남부터미널역, 양재역, 6, 5);
    }

    @Test
    void findShortestDistanceUsingKShortest_Test() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);
        List<GraphPath<Station, SectionEdge>> allPaths = subwayMap.findAllKShortestPaths(
            교대역, 양재역, PathType.DISTANCE);

        // when
        ShortestPaths shortest = subwayMap.findShortest(allPaths, START_TIME);

        // then
        assertAll(
            () -> assertThat(shortest.getShortestDistancePath()
                .getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 교대역, 남부터미널역)),
            () -> assertThat(shortest.getShortestDurationPath()
                .getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재역, 남부터미널역)),
            () -> assertThat(shortest.getShortestDistance()).isEqualTo(14),
            () -> assertThat(shortest.getShortestArrivalTime()).isEqualTo(SHORTEST_DISTANCE_ARRIVAL_TIME)
        );
    }

    @Test
    void findShortestDurationUsingKShortest_Test() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);
        List<GraphPath<Station, SectionEdge>> allPaths = subwayMap.findAllKShortestPaths(
            교대역, 양재역, PathType.DURATION);

        // when
        ShortestPaths shortest = subwayMap.findShortest(allPaths, START_TIME);

        // then
        assertAll(
            () -> assertThat(shortest.getShortestDistancePath()
                .getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 교대역, 남부터미널역)),
            () -> assertThat(shortest.getShortestDurationPath()
                .getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재역, 남부터미널역)),
            () -> assertThat(shortest.getShortestDistance()).isEqualTo(14),
            () -> assertThat(shortest.getShortestArrivalTime()).isEqualTo(SHORTEST_DURATION_ARRIVAL_TIME)
        );
    }

    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(강남역, 남부터미널역, PathType.DISTANCE, START_TIME);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 교대역, 남부터미널역));
    }

    @Test
    void findPathByDistanceOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(남부터미널역, 강남역, PathType.DISTANCE, START_TIME);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(남부터미널역, 교대역, 강남역));
    }

    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(강남역, 남부터미널역, PathType.DURATION, START_TIME);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재역, 남부터미널역));
    }

    @Test
    void findPathByDurationOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(남부터미널역, 강남역, PathType.DURATION, START_TIME);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(남부터미널역, 양재역, 강남역));
    }

    private Station createStation(long id, String name) {
        Station station = Station.of(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
