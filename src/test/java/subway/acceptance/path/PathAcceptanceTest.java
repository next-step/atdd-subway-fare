package subway.acceptance.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.acceptance.line.LineSteps;
import subway.acceptance.station.StationFixture;
import subway.utils.AcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.station.StationFixture.getStationId;

@DisplayName("경로 인수 테스트")
public class PathAcceptanceTest extends AcceptanceTest {

    /**
     * <pre>
     * 교대역  ---- *2호선* --- dt:10, dr:5 ------  강남역
     * |                                             |
     * *3호선*                                    *신분당선*
     * dt:2, dr:3                                dt:10, dr:6
     * |                                             |
     * 남부터미널역  --- *3호선* -- dt:3, dr:15 ---- 양재역
     *
     * 건대역 ------------------- dt:7, dr: 1 ------ 성수역 -------|
     *                                                          |
     *        *A호선 (긴 노선)*                                dt:3, dr:4
     *                                                          |
     * 잠실역 -- dt:25, dr:8 -- 강변역 -- dt:17, dr:8 ---- 왕십리역 ---|
     *
     * ex) 교대-양재
     * 최단거리 : 교대 - 남부터미널 - 양재
     * 최소시간 : 교대 - 강남 - 양재
     * </pre>
     */

    @BeforeEach
    void createLine() {
        StationFixture.기본_역_생성_호출();

        PathFixture.이호선_삼호선_신분당선_A호선_생성_호출();

        LineSteps.노선_목록_조회_API();
    }

    /**
     * Given 2개의 구간을 가진 노선이 있고
     * When 노선의 상행역과 하행역으로 경로를 조회하면
     * Then 3개의 역이 출력된다
     * Then 2 구간의 모든 거리의 합이 출력된다
     */
    @DisplayName("같은 노선의 구간이 최단거리인 경로를 조회한다")
    @Test
    void getShortestDistancePath() {
        // when
        var response = PathSteps.getShortestPath(getStationId("교대역"), getStationId("양재역"));

        // then
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("교대역", "남부터미널역", "양재역");

        // then
        var distance = response.jsonPath().get("distance");
        assertThat(distance).isEqualTo(5);
    }

    /**
     * Given 3개의 서로 연결된 노선이 있고
     * When 다른 노선을 모두 통과하는 경로를 조회하면
     * Then 경로 조회 결과가 나온다
     * Then 구간의 모든 거리의 합이 출력된다
     */
    @DisplayName("서로 다른 노선에 있는 구간이 최단거리인 경로를 조회한다")
    @Test
    void getShortestDistancePathWithOtherLine() {
        // when
        var response = PathSteps.getShortestPath(getStationId("강남역"), getStationId("남부터미널역"));

        // then
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("강남역", "교대역", "남부터미널역");

        // then
        var distance = response.jsonPath().get("distance");
        assertThat(distance).isEqualTo(12);
    }

    /**
     * Given 3개의 서로 연결된 노선이 있고
     * When 연결되지 않은 경로를 조회하면
     * Then 경로가 조회되지 않는다
     */
    @DisplayName("연결되지 않은 구간이 최단거리인 경로를 조회한다")
    @Test
    void getShortestDistancePathWithNotConnected() {
        // when
        var response = PathSteps.getShortestPath(getStationId("교대역"), getStationId("왕십리역"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 3개의 서로 연결된 노선이 있고
     * When 시작과 끝을 같은 역을 조회하면
     * Then 경로가 조회되지 않는다
     */
    @DisplayName("시작과 끝이 같은 역의 구간이 최단거리인 경로를 조회한다.")
    @Test
    void getShortestDistancePathWithSameStation() {
        // when
        var response = PathSteps.getShortestPath(getStationId("강남역"), getStationId("강남역"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());


    }

    /**
     * Given 3개의 서로 연결된 노선이 있고
     * When 존재 하지 않는 역으로 경로를 조회하면
     * Then 경로가 조회되지 않는다
     */
    @DisplayName("존재하지 않는 역으로 구간이 최단거리인 경로를 조회한다")
    @Test
    void getShortestDistancePathWithNotExistStation() {
        // when
        var response = PathSteps.getShortestPath(99L, 98L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    // week 4-1

    /**
     * Given 2개의 구간을 가진 노선이 있고
     * When 노선의 상행역과 하행역으로 경로를 조회하면
     * Then 3개의 역이 출력된다
     * Then 2 구간의 모든 거리의 합과 시간의 합이 출력된다
     */
    @DisplayName("같은 노선의 구간이 최소시간인 경로를 조회한다")
    @Test
    void getMinimumTimePath() {
        // when
        var response = PathSteps.getMinimumTimePath(getStationId("건대역"), getStationId("왕십리역"));

        // then
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("건대역", "성수역", "왕십리역");

        // then
        var duration = response.jsonPath().get("duration");
        assertThat(duration).isEqualTo(5);
    }

    /**
     * Given 3개의 서로 연결된 노선이 있고
     * When 다른 노선을 모두 통과하는 경로를 조회하면
     * Then 경로 조회 결과가 나온다
     * Then 구간의 모든 거리의 합과 시간의 합이 출력된다
     */
    @DisplayName("서로 다른 노선에 있는 구간이 최소시간인 경로를 조회한다")
    @Test
    void getMinimumTimePathWithOtherLine() {
        // when
        var response = PathSteps.getMinimumTimePath(getStationId("강남역"), getStationId("남부터미널역"));

        // then
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("강남역", "교대역", "남부터미널역");

        // then
        var duration = response.jsonPath().get("duration");
        assertThat(duration).isEqualTo(8);
    }

    // week 4-2

    /**
     * 1~10  : 1250
     * 11~15 : 1350
     * 16~20 : 1450
     * 21~25 : 1550
     * 26~30 : 1660
     * 31~35 : 1750
     * 36~40 : 1850
     * 41~45 : 1950
     * 46~50 : 2050
     * 51~58 : 2150
     * 59~.. : 2250
     */

    /**
     * Given 2개의 구간을 가진 노선이 있고
     * When 노선의 상행역과 하행역으로 경로를 조회하면
     * Then 3개의 역이 출력된다
     * Then 운임이 출력된다.
     */
    @DisplayName("기본운임을 넘지 않는 경로의 운임을 조회한다")
    @Test
    void getPathFareInDefaultFare() {
        // when
        var response = PathSteps.getMinimumTimePath(getStationId("건대역"), getStationId("왕십리역"));

        // then
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("건대역", "성수역", "왕십리역");

        // then
        var fare = response.jsonPath().get("fare");
        assertThat(fare).isEqualTo(1250);
    }

    /**
     * Given 2개의 구간을 가진 노선이 있고
     * When 노선의 상행역과 하행역으로 경로를 조회하면
     * Then 운임이 출력된다.
     */
    @DisplayName("기본운임이 넘고 50km 미만인 경로의 운임을 조회한다")
    @Test
    void getPathFareOverDefaultFare() {
        // when
        var response = PathSteps.getShortestPath(getStationId("건대역"), getStationId("강변역"));

        // then
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("건대역", "성수역", "왕십리역", "강변역");

        // then
        var fare = response.jsonPath().get("fare");
        assertThat(fare).isEqualTo(1650);
    }

    /**
     * Given 2개의 구간을 가진 노선이 있고
     * When 노선의 상행역과 하행역으로 경로를 조회하면
     * Then 운임이 출력된다.
     */
    @DisplayName("기본운임이 넘고 50km 미만인 경로의 최소시간 경로와 최단거리 경로가 다른 운임을 조회한다")
    @Test
    void getPathFareOverDefaultFareWithDifferentWeight() {
        // when
        var response = PathSteps.getMinimumTimePath(getStationId("교대역"), getStationId("양재역"));

        // then
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("교대역", "강남역", "양재역");

        // then
        var fare = response.jsonPath().get("fare");
        assertThat(fare).isEqualTo(1250);
    }

    /**
     * Given 2개의 구간을 가진 노선이 있고
     * When 노선의 상행역과 하행역으로 경로를 조회하면
     * Then 3개의 역이 출력된다
     * Then 운임이 출력된다.
     */
    @DisplayName("기본운임이 넘고 50km 초과인 경로의 운임을 조회한다")
    @Test
    void getPathFareOverFiftyKm() {
        // when
        var response = PathSteps.getShortestPath(getStationId("건대역"), getStationId("잠실역"));

        // then
        List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("건대역", "성수역", "왕십리역", "강변역","잠실역");

        // then
        var fare = response.jsonPath().get("fare");
        assertThat(fare).isEqualTo(2150);
    }
}
