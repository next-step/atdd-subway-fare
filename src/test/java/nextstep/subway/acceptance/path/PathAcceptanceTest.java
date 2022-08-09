package nextstep.subway.acceptance.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.line.LineSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.line.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.path.PathSteps.경로_조회_응답_검증;
import static nextstep.subway.acceptance.path.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.path.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.station.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 서울역;
    private Long 시청역;

    private Long 일호선;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 사호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        서울역 = 지하철역_생성_요청(관리자, "서울역").jsonPath().getLong("id");
        시청역 = 지하철역_생성_요청(관리자, "시청역").jsonPath().getLong("id");

        일호선 = 지하철_노선_생성_요청("1호선", "blue", 서울역, 시청역, 10, 1);
        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 15, 2);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 15, 2);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 7, 4);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 5, 2));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(관리자), 교대역, 양재역);

        // then
        경로_조회_응답_검증(response, 12, 6, 1350, 교대역, 남부터미널역, 양재역);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(관리자), 교대역, 양재역);

        // then
        경로_조회_응답_검증(response, 30, 4, 1650, 교대역, 강남역, 양재역);
    }

    @DisplayName("등록되지 않은 지하철역을 경로 조회 요청하면 예외 발생")
    @Test
    void findPathByDurationWithUnknownStation() {
        // given
        Long 등록되지_않은_지하철역 = 1234L;

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(관리자), 등록되지_않은_지하철역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이어지지 않은 두 역의 경로를 조회하면 예외 발생")
    @Test
    void findPathByDistanceWithoutConnection() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(관리자), 서울역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("findPath 메서드는")
    @Nested
    class Describe_findPath {

        private Long 지하철A역;
        private Long 지하철B역;
        private Long 지하철C역;
        private Long 지하철D역;
        private Long 지하철E역;
        private Long 지하철F역;
        private Long 지하철G역;
        private Long 지하철H역;
        private Long 지하철I역;

        @BeforeEach
        void setUp() {
            지하철A역 = 지하철역_생성_요청(관리자, "지하철A역").jsonPath().getLong("id");
            지하철B역 = 지하철역_생성_요청(관리자, "지하철B역").jsonPath().getLong("id");
            지하철C역 = 지하철역_생성_요청(관리자, "지하철C역").jsonPath().getLong("id");
            지하철D역 = 지하철역_생성_요청(관리자, "지하철D역").jsonPath().getLong("id");
            지하철E역 = 지하철역_생성_요청(관리자, "지하철E역").jsonPath().getLong("id");
            지하철F역 = 지하철역_생성_요청(관리자, "지하철F역").jsonPath().getLong("id");
            지하철G역 = 지하철역_생성_요청(관리자, "지하철G역").jsonPath().getLong("id");
            지하철H역 = 지하철역_생성_요청(관리자, "지하철H역").jsonPath().getLong("id");
            지하철I역 = 지하철역_생성_요청(관리자, "지하철I역").jsonPath().getLong("id");
        }

        @DisplayName("4개의 역을 환승을 두 번하고 20분 경로와 환승 없이 7개의 역을 25분 경로를")
        @Nested
        class Context_with_Two_Condition_Diff_Transfer {

            @BeforeEach
            void setUp() {
                지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 25, 8);
                지하철_노선_생성_요청("노선B", "black", 지하철B역, 지하철C역, 20, 6);
                지하철_노선_생성_요청("노선C", "red", 지하철C역, 지하철D역, 20, 6);

                Long 노선D = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 10, 6);
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철E역, 지하철F역, 10, 6));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철F역, 지하철G역, 8, 4));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철G역, 지하철H역, 8, 4));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철H역, 지하철I역, 6, 3));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철I역, 지하철D역, 4, 2));
            }

            @DisplayName("최소시간 단위로 조회하면, 거리는 65km가 되고, 20분이 걸리고, 2250원의 요금이 계산되며, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithTwoConditionByDuration(){
                // when
                ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(관리자), 지하철A역, 지하철D역);

                // then
                final int 거리_65km = 65;
                final int 시간_20분 = 20;
                final int 요금_2250원 = 2250;
                경로_조회_응답_검증(response, 거리_65km, 시간_20분, 요금_2250원, 지하철A역, 지하철B역, 지하철C역, 지하철D역);
            }

            @DisplayName("최단거리 단위로 조회하면, 거리는 46km가 되고, 25분이 걸리며, 1750원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithTwoConditionByDistance(){
                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(관리자), 지하철A역, 지하철D역);

                // then
                final int 거리_46km = 46;
                final int 시간_25분 = 25;
                final int 요금_2050원 = 2050;
                경로_조회_응답_검증(response, 거리_46km, 시간_25분, 요금_2050원, 지하철A역, 지하철E역, 지하철F역, 지하철G역, 지하철H역, 지하철I역, 지하철D역);
            }
        }

        @DisplayName("4개의 역을 가야하지만 소요시간이 10분 경로와, 3개의 역을 가지만 소요시간이 20분인 경로를")
        @Nested
        class Context_with_Two_Condition_Diff_Distance_And_Duration {

            @BeforeEach
            void setUp() {
                Long 노선A = 지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 10, 5);
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철B역, 지하철C역, 10, 5));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철C역, 지하철D역, 10, 5));

                Long 노선B = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 12, 10);
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선B, createSectionCreateParams(지하철E역, 지하철D역, 12, 10));
            }

            @DisplayName("최소시간 단위로 조회하면, 거리는 30km가 되고, 15분이 걸리고, 1650원의 요금이 계산되며, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithTwoConditionByDuration(){
                // when
                ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(관리자), 지하철A역, 지하철D역);

                // then
                final int 거리_30km = 30;
                final int 시간_15분 = 15;
                final int 요금_1650원 = 1650;
                경로_조회_응답_검증(response, 거리_30km, 시간_15분, 요금_1650원, 지하철A역, 지하철B역, 지하철C역, 지하철D역);
            }

            @DisplayName("최단거리 단위로 조회하면, 거리는 24km가 되고, 20분이 걸리며, 1550원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithTwoConditionByDistance(){
                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(관리자), 지하철A역, 지하철D역);

                // then
                final int 거리_24km = 24;
                final int 시간_20분 = 20;
                final int 요금_1550원 = 1550;
                경로_조회_응답_검증(response, 거리_24km, 시간_20분, 요금_1550원, 지하철A역, 지하철E역, 지하철D역);
            }
        }

        @DisplayName("4개의 역을 가야하지만 소요시간이 10분 경로와 4개의 역을 가야하지만 15분인 경로를")
        @Nested
        class Context_with_Two_Condition_Diff_Duration {

            @BeforeEach
            void setUp() {
                Long 노선A = 지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 2, 5);
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철B역, 지하철C역, 3, 5));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철C역, 지하철D역, 2, 5));

                Long 노선B = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 2, 3);
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선B, createSectionCreateParams(지하철E역, 지하철F역, 3, 3));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선B, createSectionCreateParams(지하철F역, 지하철D역, 3, 4));
            }

            @DisplayName("최소시간 단위로 조회하면, 거리는 8km가 되고, 10분이 걸리고, 1250원의 요금이 계산되며, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithTwoConditionByDuration(){
                // when
                ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(관리자), 지하철A역, 지하철D역);

                // then
                final int 거리_8km = 8;
                final int 시간_10분 = 10;
                final int 요금_1250원 = 1250;
                경로_조회_응답_검증(response, 거리_8km, 시간_10분, 요금_1250원, 지하철A역, 지하철E역, 지하철F역, 지하철D역);
            }

            @DisplayName("최단거리 단위로 조회하면, 거리는 7km가 되고, 15분이 걸리며, 1250원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithTwoConditionByDistance(){
                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(관리자), 지하철A역, 지하철D역);

                // then
                final int 거리_7km = 7;
                final int 시간_15분 = 15;
                final int 요금_1250원 = 1250;
                경로_조회_응답_검증(response, 거리_7km, 시간_15분, 요금_1250원, 지하철A역, 지하철B역, 지하철C역, 지하철D역);
            }
        }

        @DisplayName("900원 추가요금이 있는 노선을")
        @Nested
        class Context_with_Over_Pay {

            @BeforeEach
            void setUp() {
                지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 4, 8);
                지하철_노선_생성_요청("노선B", "black", 지하철B역, 지하철C역, 2, 6, 900);
                지하철_노선_생성_요청("노선C", "red", 지하철C역, 지하철D역, 2, 6);
                지하철_노선_생성_요청("노선D", "yellow", 지하철E역, 지하철B역, 5, 7);
                지하철_노선_생성_요청("노선E", "green", 지하철C역, 지하철F역, 5, 8);
            }

            @DisplayName("8Km 이용하는 경로를 최단거리 단위로 조회하면 2,150원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithOverPayByDistance8Km() {
                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(관리자), 지하철A역, 지하철D역);

                // then
                final int 거리_8km = 8;
                final int 시간_20분 = 20;
                final int 요금_2150원 = 2150;
                경로_조회_응답_검증(response, 거리_8km, 시간_20분, 요금_2150원, 지하철A역, 지하철B역, 지하철C역, 지하철D역);
            }

            @DisplayName("12Km 이용하는 경로를 최단거리 단위로 조회하면 2,250원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithOverPayByDistance12Km() {
                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(관리자), 지하철E역, 지하철F역);

                // then
                final int 거리_12km = 12;
                final int 시간_21분 = 21;
                final int 요금_2250원 = 2250;
                경로_조회_응답_검증(response, 거리_12km, 시간_21분, 요금_2250원, 지하철E역, 지하철B역, 지하철C역, 지하철F역);
            }
        }

        @DisplayName("900원, 500원 추가요금이 있는 노선을 경유하여")
        @Nested
        class Context_with_Over_Pays {

            @BeforeEach
            void setUp() {
                지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 4, 8);
                지하철_노선_생성_요청("노선B", "black", 지하철B역, 지하철C역, 2, 6, 900);
                지하철_노선_생성_요청("노선C", "red", 지하철C역, 지하철D역, 2, 6, 500);
            }

            @DisplayName("8Km 이용하는 경로를 최단거리 단위로 조회하면 2,150원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWithOverPaysByDistance8Km() {
                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(관리자), 지하철A역, 지하철D역);

                // then
                final int 거리_8km = 8;
                final int 시간_20분 = 20;
                final int 요금_2150원 = 2150;
                경로_조회_응답_검증(response, 거리_8km, 시간_20분, 요금_2150원, 지하철A역, 지하철B역, 지하철C역, 지하철D역);
            }
        }

        @DisplayName("환승없는 7개 역을 가는데 로그인한 사용자의 연령이")
        @Nested
        class Context_with_Age_of_Login_User {

            @BeforeEach
            void setUp() {
                Long 노선D = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 10, 6);
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철E역, 지하철F역, 10, 6));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철F역, 지하철G역, 8, 4));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철G역, 지하철H역, 8, 4));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철H역, 지하철I역, 6, 3));
                지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철I역, 지하철D역, 4, 2));
            }

            @DisplayName("5세인 사용자가 최단 거리 경로를 조회하면 0원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWith5AgeByDistance() {
                // given
                String accessToken = 로그인_되어_있음("age5@email.com", "password");

                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(accessToken), 지하철A역, 지하철D역);

                // then
                final int 거리_46km = 46;
                final int 시간_25분 = 25;
                final int 요금_0원 = 0;
                경로_조회_응답_검증(response, 거리_46km, 시간_25분, 요금_0원, 지하철A역, 지하철E역, 지하철F역, 지하철G역, 지하철H역, 지하철I역, 지하철D역);
            }

            @DisplayName("6세인 사용자가 최단 거리 경로를 조회하면 850원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWith6AgeByDistance() {
                // given
                String accessToken = 로그인_되어_있음("age6@email.com", "password");

                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(accessToken), 지하철A역, 지하철D역);

                // then
                final int 거리_46km = 46;
                final int 시간_25분 = 25;
                final int 요금_850원 = 850;
                경로_조회_응답_검증(response, 거리_46km, 시간_25분, 요금_850원, 지하철A역, 지하철E역, 지하철F역, 지하철G역, 지하철H역, 지하철I역, 지하철D역);
            }

            @DisplayName("12세인 사용자가 최단 거리 경로를 조회하면 850원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWith12AgeByDistance() {
                // given
                String accessToken = 로그인_되어_있음("age12@email.com", "password");

                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(accessToken), 지하철A역, 지하철D역);

                // then
                final int 거리_46km = 46;
                final int 시간_25분 = 25;
                final int 요금_850원 = 850;
                경로_조회_응답_검증(response, 거리_46km, 시간_25분, 요금_850원, 지하철A역, 지하철E역, 지하철F역, 지하철G역, 지하철H역, 지하철I역, 지하철D역);
            }

            @DisplayName("13세인 사용자가 최단 거리 경로를 조회하면 1360원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWith13AgeByDistance() {
                // given
                String accessToken = 로그인_되어_있음("age13@email.com", "password");

                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(accessToken), 지하철A역, 지하철D역);

                // then
                final int 거리_46km = 46;
                final int 시간_25분 = 25;
                final int 요금_1360원 = 1360;
                경로_조회_응답_검증(response, 거리_46km, 시간_25분, 요금_1360원, 지하철A역, 지하철E역, 지하철F역, 지하철G역, 지하철H역, 지하철I역, 지하철D역);
            }

            @DisplayName("18세인 사용자가 최단 거리 경로를 조회하면 1360원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWith18AgeByDistance() {
                // given
                String accessToken = 로그인_되어_있음("age18@email.com", "password");

                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(accessToken), 지하철A역, 지하철D역);

                // then
                final int 거리_46km = 46;
                final int 시간_25분 = 25;
                final int 요금_1360원 = 1360;
                경로_조회_응답_검증(response, 거리_46km, 시간_25분, 요금_1360원, 지하철A역, 지하철E역, 지하철F역, 지하철G역, 지하철H역, 지하철I역, 지하철D역);
            }

            @DisplayName("19세인 사용자가 최단 거리 경로를 조회하면 2050원의 요금이 계산되고, 유효한 지하철역 목록을 반환한다.")
            @Test
            void findPathWith19AgeByDistance() {
                // given
                String accessToken = 로그인_되어_있음("age19@email.com", "password");

                // when
                ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(accessToken), 지하철A역, 지하철D역);

                // then
                final int 거리_46km = 46;
                final int 시간_25분 = 25;
                final int 요금_2050원 = 2050;
                경로_조회_응답_검증(response, 거리_46km, 시간_25분, 요금_2050원, 지하철A역, 지하철E역, 지하철F역, 지하철G역, 지하철H역, 지하철I역, 지하철D역);
            }
        }

    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int overFare) {
        Map<String, String> lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("additionalFare", overFare + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

}
