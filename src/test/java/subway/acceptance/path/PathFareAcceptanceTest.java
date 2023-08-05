package subway.acceptance.path;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.acceptance.line.LineSteps;
import subway.acceptance.station.StationFixture;
import subway.utils.AcceptanceTest;

@SuppressWarnings("NonAsciiCharacters")
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

    @Nested
    @DisplayName("경로")
    class PathTest {
        @BeforeEach
        void createLine() {
            StationFixture.기본_역_생성_호출();
            PathFixture.노선_요금을_가진_이호선_삼호선_신분당선_A호선_생성_호출();
        }

        @Nested
        @DisplayName("")
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
        @DisplayName("")
        class DiscountFareWithPolicyOfAgeInMember {

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
}