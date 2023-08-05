package subway.acceptance.fare;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.acceptance.line.LineSteps;
import subway.acceptance.path.PathFixture;
import subway.acceptance.path.PathSteps;
import subway.acceptance.station.StationFixture;
import subway.utils.AcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.station.StationFixture.getStationId;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("운임 인수 테스트")
public class PathFareAcceptanceTest extends AcceptanceTest {

    // week 4-3

    /**
     * <pre>
     * 교대역 ----- *2호선:100원* ----- 길이:10, 시간:5 ------ 강남역
     * |                                                  |
     * |                                           *신분당선:1200원*
     * 길이:2, 시간:3                                  길이:10, 시간:6
     * |                                                  |
     * 남부터미널역  ------ *3호선:500원* 길이:3, 시간:15 ------ 양재역
     *
     * 건대역 ---- *A호선 (긴 노선):0원* --- 길이:7, 시간: 1 ---- 성수역
     *                                                      |
     *                                                 길이:3, 시간:4
     *                                                      |
     * 잠실역 -- 길이:25, 시간:8 -- 강변역 -- 길이:17, 시간:8 ---- 왕십리역
     *
     * ex) 교대-양재
     * 최단거리 : 교대 - 남부터미널 - 양재
     * 최소시간 : 교대 - 강남 - 양재
     * </pre>
     */

    @BeforeEach
    void createLine() {
        StationFixture.기본_역_생성_호출();
        PathFixture.노선_요금을_가진_이호선_삼호선_신분당선_A호선_생성_호출();
    }

    @Nested
    @DisplayName("구간 추가 과금 테스트")
    class Distance {
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
            assertThat(fare).isEqualTo(1750);
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
            assertThat(list).containsExactlyInAnyOrder("건대역", "성수역", "왕십리역", "강변역", "잠실역");

            // then
            var fare = response.jsonPath().get("fare");
            assertThat(fare).isEqualTo(2150);
        }
    }

    @Nested
    @DisplayName("노선 기본 과금 테스트")
    class LineSurcharge {
        /**
         * Given 추가운임이 있는 노선이 있고
         * When 경로를 조회하면
         * Then 추가운임이 적용된 총 운임이 출력된다.
         */
        @DisplayName("노선의 추가운임이 포함된 경로의 운임을 조회한다.")
        @Test
        void test() {
            LineSteps.노선_목록_조회_API();
        }

        /**
         * Given 추가운임이 있는 노선이 있고
         * When 여러개의 노선을 지나가는 경로를 조회하면
         * Then 노선의 추가운임 중 가장 높은 추가운임 하나만 적용된 총 운임이 출력된다.
         */
        @DisplayName("")
        @Test
        void test1() {

        }

        /**
         * Given 추가운임이 있는 노선이 있고
         * When 여러개의 노선을 지나가는 경로를 조회하면
         * Then 노선의 추가운임 중 가장 높은 추가운임 하나만 적용된 총 운임이 출력된다.
         */
        @DisplayName("")
        @Test
        void test2() {

        }

    }

    @Nested
    @DisplayName("회원 할인 테스트")
    class MemberAge {

        /**
         * Given 추가운임이 있는 노선이 있고
         * When 회원의 나이가 13~19사이 일 때
         * When 여러개의 노선을 지나가는 경로를 조회하면
         * Then 350원을 공제한 금액의 20%가 감면된 금액이 출력된다.
         */
        @DisplayName("")
        @Test
        void test3() {

        }

        /**
         * Given 추가운임이 있는 노선이 있고
         * When 회원의 나이가 6~13사이 일 때
         * When 여러개의 노선을 지나가는 경로를 조회하면
         * Then 350원을 공제한 금액의 30%가 감면된 금액이 출력된다.
         */
        @DisplayName("")
        @Test
        void test4() {

        }
    }


}