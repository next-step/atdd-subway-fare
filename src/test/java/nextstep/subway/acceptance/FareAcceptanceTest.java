package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("지하철 요금 계산")
public class FareAcceptanceTest extends AcceptanceTest {
    private static final int DEDUCTIBLE_FARE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 선릉역;
    private Long 선정릉역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 추가요금선;
    private Long 높은추가요금선;

    /**
     * 교대역    --- *2호선(10km, 5분)* ---   강남역
     * |                                     |
     * *3호선(2km, 4분)*                   *신분당선(11km, 3분)*
     * |                                     |
     * 남부터미널역  --- *3호선(3km, 8분)* ---   양재
     *                                       |
     *                                  *추가요금선(13km, 10분, 500원)*
     *                                       |
     *                                     선릉역
     *                                       |
     *                                  *높은추가요금선(12km, 15분, 900원)*
     *                                       |
     *                                     선정릉역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        선릉역 = 지하철역_생성_요청(관리자, "선릉역").jsonPath().getLong("id");
        선정릉역 = 지하철역_생성_요청(관리자, "선정릉역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 5, 0);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 11, 3, 0);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 4,0);
        추가요금선 = 지하철_노선_생성_요청("추가요금선", "blue", 양재역, 선릉역, 13, 10,500);
        높은추가요금선 = 지하철_노선_생성_요청("높은추가요금선", "yellow", 선릉역, 선정릉역, 12, 15,900);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 8));
    }

    /**
     * when 두 역의 최단 거리 경로 요금을 조회하면,
     * then 최단 경로 요금이 적용된다.
     */
    @DisplayName("두 역의 최단 거리 경로 요금을 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE");

        // then
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(1250);
    }

    /**
     * when 두 역의 최소 시간 경로 요금을 조회하면,
     * then 최단 경로 요금이 적용된다.
     */
    @DisplayName("두 역의 최소 시간 경로 요금을 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DURATION");

        // then
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(1250);
    }

    /**
     * when 1게의 추가 요금이 있는 노선을 거쳐가는 두 역의 최단 거리 경로를 조회하면,
     * then 추가 요금이 있는 노선의 요금이 적용된다.
     */
    @DisplayName("추가 요금이 있는 노선 1개를 이용 할 경우 측정된 요금에 추가")
    @Test
    void 추가_요금_노선_1개_이용시_추가요금_적용() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 선릉역, "DISTANCE");

        // then
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(1450 + 500);
    }

    /**
     * when 2개의 추가 요금이 있는 노선을 거쳐가는 두 역의 최단 거리 경로를 조회하면,
     * then 가장 높은 추가 요금이 있는 노선의 요금만 적용된다.
     */
    @DisplayName("추가 요금이 있는 노선을 2개 이용 할 경우 측정된 요금에 추가")
    @Test
    void 추가_요금_노선_2개_이용시_가장_높은_추가요금_적용() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 선정릉역, "DISTANCE");

        // then
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(1650 + 900);
    }

    @Nested
    @DisplayName("어린이")
    class Child {
        /**
         * given 어린이로 로그인되어 있고,
         * when 두 역의 최단 거리 경로를 조회하면,
         * then 할인된 요금이 적용된다.
         */
        @DisplayName("어린이는 운임에서 할인된 요금이 적용된다.")
        @Test
        void 어린이_사용자가_지하철을_이용하면_할인된_요금_적용() {
            // when
            ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(어린이, 교대역, 양재역, "DISTANCE");

            // then
            assertThat(response.jsonPath().getLong("fare")).isEqualTo(운임예_어린이_요금_적용(1250));
        }

        /**
         * given 어린이로 로그인되어 있고,
         * when 1게의 추가 요금이 있는 노선을 거쳐가는 두 역의 최단 거리 경로를 조회하면,
         * then 추가 요금이 있는 노선의 요금이 적용된다.
         */
        @DisplayName("추가 요금이 있는 노선 1개를 이용 할 경우 측정된 요금에 추가")
        @Test
        void 어린이_사용자가_추가_요금_노선_1개_이용시_추가요금_적용() {
            // when
            ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(어린이, 교대역, 선릉역, "DISTANCE");

            // then
            assertThat(response.jsonPath().getLong("fare")).isEqualTo(운임예_어린이_요금_적용(1450 + 500));
        }

        /**
         * given 어린이로 로그인되어 있고,
         * when 2개의 추가 요금이 있는 노선을 거쳐가는 두 역의 최단 거리 경로를 조회하면,
         * then 가장 높은 추가 요금이 있는 노선의 요금만 적용된다.
         */
        @DisplayName("추가 요금이 있는 노선을 2개 이용 할 경우 측정된 요금에 추가")
        @Test
        void 어린이_사용자가_추가_요금_노선_2개_이용시_가장_높은_추가요금_적용() {
            // when
            ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(어린이, 교대역, 선정릉역, "DISTANCE");

            // then
            assertThat(response.jsonPath().getLong("fare")).isEqualTo(운임예_어린이_요금_적용(1650 + 900));
        }
    }

    @Nested
    @DisplayName("어린이")
    class Teenager {
        /**
         * given 청소년으로 로그인되어 있고,
         * when 두 역의 최단 거리 경로를 조회하면,
         * then 할인된 요금이 적용된다.
         */
        @DisplayName("청소년은 운임에서 할인된 요금이 적용된다.")
        @Test
        void 청소년_사용자가_지하철을_이용하면_할인된_요금_적용() {
            // when
            ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(청소년, 교대역, 양재역, "DISTANCE");

            // then
            assertThat(response.jsonPath().getLong("fare")).isEqualTo(운임예_청소년_요금_적용(1250));
        }

        /**
         * given 청소년으로 로그인되어 있고,
         * when 1게의 추가 요금이 있는 노선을 거쳐가는 두 역의 최단 거리 경로를 조회하면,
         * then 추가 요금이 있는 노선의 요금이 적용된다.
         */
        @DisplayName("추가 요금이 있는 노선 1개를 이용 할 경우 측정된 요금에 추가")
        @Test
        void 청소년_사용자가_추가_요금_노선_1개_이용시_추가요금_적용() {
            // when
            ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(청소년, 교대역, 선릉역, "DISTANCE");

            // then
            assertThat(response.jsonPath().getLong("fare")).isEqualTo(운임예_청소년_요금_적용(1450 + 500));
        }

        /**
         * given 청소년으로 로그인되어 있고,
         * when 2개의 추가 요금이 있는 노선을 거쳐가는 두 역의 최단 거리 경로를 조회하면,
         * then 가장 높은 추가 요금이 있는 노선의 요금만 적용된다.
         */
        @DisplayName("추가 요금이 있는 노선을 2개 이용 할 경우 측정된 요금에 추가")
        @Test
        void 청소년_사용자가_추가_요금_노선_2개_이용시_가장_높은_추가요금_적용() {
            // when
            ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(청소년, 교대역, 선정릉역, "DISTANCE");

            // then
            assertThat(response.jsonPath().getLong("fare")).isEqualTo(운임예_청소년_요금_적용(1650 + 900));
        }
    }

    private int 운임예_어린이_요금_적용(int originFare) {
        // 요금 공제
        int fare = originFare - DEDUCTIBLE_FARE;
        // 할인율 적용
        fare *= (1 - CHILD_DISCOUNT_RATE);
        return fare;
    }

    private int 운임예_청소년_요금_적용(int originFare) {
        // 요금 공제
        int fare = originFare - DEDUCTIBLE_FARE;
        // 할인율 적용
        fare *= (1 - TEENAGER_DISCOUNT_RATE);
        return fare;
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int surCharge) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("surcharge", surCharge + "");

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
