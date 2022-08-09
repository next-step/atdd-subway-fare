package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.path.SubwayMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 수원역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        수원역 = createStation(5L, "수원역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 10, 1);
        이호선.addSection(교대역, 강남역, 10, 1);
        삼호선.addSection(교대역, 남부터미널역, 2, 10);
        삼호선.addSection(남부터미널역, 양재역, 3, 5);

        /**
         * 교대역    --- *2호선(10   , 1)* ---   강남역
         * |                                |
         * *3호선(2, 10)*                     *신분당선* (10, 1)
         * |                                |
         * 남부터미널역  --- *3호선(3,   5)* ---  양재
         */
    }

    /**
     *   Feature: 환승하지 않고 최단 거리 구하는 케이스
     *
     *   Scenario: 환승하지 않고 최단거리 구하기
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 교대역, 양재역 최단 거리를 구합니다.
     *     Then 최단경로를 비교 합니다.
     */
    @DisplayName("환승하지 않고 최단 거리 구하는 케이스")
    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE.getEdgeInitiator());

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }

    /**
     *   Feature: 환승하고 최단 거리 구하는 케이스
     *
     *   Scenario: 환승하고 최단거리 구하기
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 교대역, 양재역 최단 거리를 구합니다.
     *     Then 최단경로를 비교 합니다.
     */
    @DisplayName("환승하고 최단 거리 구하는 케이스")
    @Test
    void TransferFindPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(강남역, 남부터미널역, PathType.DISTANCE.getEdgeInitiator());

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 교대역, 남부터미널역));
    }

    /**
     *   Feature: 출퇴근 왕복 경로 최단거리 구하는 케이스
     *
     *   Scenario: 황복 최단거리 구하기
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 출근 할떄 최단 경로를 구합니다.
     *     When 퇵근 할때 최단 경로를 구합니다.
     *     Then 출근 최단경로를 비교 합니다.
     *     Then 퇴근 최단경로를 비교 합니다.
     */
    @DisplayName("출퇴근 왕복 경로 최단거리 구하는 케이스")
    @Test
    void RoundTripFindPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path 출근 = subwayMap.findPath(강남역, 남부터미널역, PathType.DISTANCE.getEdgeInitiator());
        Path 퇴근 = subwayMap.findPath(남부터미널역, 강남역, PathType.DISTANCE.getEdgeInitiator());

        // then
        assertThat(출근.getStations()).containsExactlyElementsOf(Lists.newArrayList(강남역, 교대역, 남부터미널역));
        assertThat(퇴근.getStations()).containsExactlyElementsOf(Lists.newArrayList(남부터미널역, 교대역, 강남역));
    }

    /**
     *   Feature: 출발지와 도착지를 같은 곳을 했을 경우
     *
     *   Scenario: 출발지, 도착지 같은 최단거리 구하기
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 최단 경로를 찾습니다.
     *     Then 최단 경로를 비교합니다.
     */
    @DisplayName("출발지와 도착지를 같은 곳을 했을 경우")
    @Test
    void equalsStartStationAndFinishStationFindPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(강남역, 강남역, PathType.DISTANCE.getEdgeInitiator());

        assertThat(path.getStations()).hasSize(0);
    }

    /**
     *   Feature: 연결되지 않은 지하철역일 경우 에러를 반환합니다.
     *
     *   Scenario: 연결되지 않는 지하철역 최단거리 구하기
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 가는 길을 찾지 못하여 에러를 반환합니다.
     */
    @DisplayName("연결되어 있지 않는 지하철역일 경우 에러를 반환합니다.")
    @Test
    void notConnectionFindPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            subwayMap.findPath(강남역, 수원역, PathType.DISTANCE.getEdgeInitiator());
        });
    }

    /**
     *   Feature: 환승하여 최소 시간 구간 구하는 케이스
     *
     *   Scenario: 환승하여 최소 시간 구간 구합니다.
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 최소 시간이 걸리는 경로를 찾습니다.
     *     Then 경로를 비교합니다.
     */
    @DisplayName("환승하여 최소 시간 구간 구하는 케이스")
    @Test
    void TransferFindPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DURATION.getEdgeInitiator());

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    /**
     *   Feature: 환승없이 최소 시간 구간 구하는 케이스
     *
     *   Scenario: 환승없이 최소 시간 구간 구합니다.
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 최소 시간이 걸리는 경로를 찾습니다.
     *     Then 경로를 비교합니다.
     */
    @DisplayName("환승없이 최소 시간 구간 구하는 케이스")
    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 강남역, PathType.DURATION.getEdgeInitiator());

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역));
    }

    /**
     *   Feature: 출발역과 도착역이 같은 최소 시간 구하는 케이스
     *
     *   Scenario: 출발지, 도착지가 같은 케이스
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 최소 시간이 걸리는 경로를 찾습니다.
     *     Then 경로를 비교합니다.
     */
    @DisplayName("출발역과 도착역이 같은 최소 시간 구하는 케이스")
    @Test
    void equalsStartStationAndFinishStationfindPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 교대역, PathType.DURATION.getEdgeInitiator());

        // then
        assertThat(path.getStations()).hasSize(0);
    }

    /**
     *   Feature: 연결되지 않은역의 최소 거리를 조회시 에러를 반환합니다.
     *
     *   Scenario: 연결되지 않은역의 최소 거리 조회
     *     Given 지하철 역이 등록되어있다.
     *     Given subwayMap에 지하철노선들을 등록합니다.
     *     When 최소 시간이 걸리는 경로를 찾는데, 경로를 찾지 못해 에러를 반환합니다.
     */
    @DisplayName("연결되지 않은역의 최소 거리를 조회시 에러를 반환합니다.")
    @Test
    void notConnectionFindPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            subwayMap.findPath(교대역, 수원역, PathType.DURATION.getEdgeInitiator());
        });
    }


    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
