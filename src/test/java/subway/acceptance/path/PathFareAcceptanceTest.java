package subway.acceptance.path;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.acceptance.line.LineSteps;
import subway.acceptance.station.StationFixture;
import subway.utils.AcceptanceTest;

public class PathFareAcceptanceTest extends AcceptanceTest {

    // week 4-3
    // TODO : Path 인수 테스트랑 합쳐야해요.

    @Nested
    @DisplayName("")
    class PathTest {
        @BeforeEach
        void createLine() {
            StationFixture.기본_역_생성_호출();
            PathFixture.이호선_삼호선_신분당선_A호선_생성_호출();
        }

        @Nested
        @DisplayName("")
        class LineDefaultFare {
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