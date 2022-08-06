package nextstep.subway.acceptance.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.line.LineSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.line.LineSteps.지하철_노선에_지하철_구간_생성_요청;
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

    private Long 지하철A역;
    private Long 지하철B역;
    private Long 지하철C역;
    private Long 지하철D역;
    private Long 지하철E역;
    private Long 지하철F역;
    private Long 지하철G역;
    private Long 지하철H역;
    private Long 지하철I역;

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

        지하철A역 = 지하철역_생성_요청(관리자, "지하철A역").jsonPath().getLong("id");
        지하철B역 = 지하철역_생성_요청(관리자, "지하철B역").jsonPath().getLong("id");
        지하철C역 = 지하철역_생성_요청(관리자, "지하철C역").jsonPath().getLong("id");
        지하철D역 = 지하철역_생성_요청(관리자, "지하철D역").jsonPath().getLong("id");
        지하철E역 = 지하철역_생성_요청(관리자, "지하철E역").jsonPath().getLong("id");
        지하철F역 = 지하철역_생성_요청(관리자, "지하철F역").jsonPath().getLong("id");
        지하철G역 = 지하철역_생성_요청(관리자, "지하철G역").jsonPath().getLong("id");
        지하철H역 = 지하철역_생성_요청(관리자, "지하철H역").jsonPath().getLong("id");
        지하철I역 = 지하철역_생성_요청(관리자, "지하철I역").jsonPath().getLong("id");

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
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(), 교대역, 양재역);

        // then
        경로_조회_응답_검증(response, 12, 6, 1350, 교대역, 남부터미널역, 양재역);
    }

    @DisplayName("4개의 역을 가야하지만 소요시간이 10분 경로와, 3개의 역을 가지만 소요시간이 20분인 경우 거리 기준 경로 조회")
    @Test
    void findPathByDistanceBetweenDifferentSection() {
        // given
        Long 노선A = 지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 10, 5);
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철B역, 지하철C역, 10, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철C역, 지하철D역, 10, 5));

        Long 노선B = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 12, 10);
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선B, createSectionCreateParams(지하철E역, 지하철D역, 12, 10));

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(), 지하철A역, 지하철D역);

        // then
        경로_조회_응답_검증(response, 24, 20, 1550, 지하철A역, 지하철E역, 지하철D역);
    }

    @DisplayName("4개의 역을 환승을 두 번하고 20분 경로와 환승 없이 7개의 역을 25분경로 거리 기준 경로 조회")
    @Test
    void findPathByDistanceBetween() {
        // given
        지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 15, 8);
        지하철_노선_생성_요청("노선B", "black", 지하철B역, 지하철C역, 10, 6);
        지하철_노선_생성_요청("노선C", "red", 지하철C역, 지하철D역, 10, 6);

        Long 노선D = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 10, 5);
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철E역, 지하철F역, 15, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철F역, 지하철G역, 7, 4));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철G역, 지하철H역, 8, 4));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철H역, 지하철I역, 6, 4));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철I역, 지하철D역, 4, 3));

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(), 지하철A역, 지하철D역);

        // then
        경로_조회_응답_검증(response, 35, 20, 1750, 지하철A역, 지하철B역, 지하철C역, 지하철D역);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(), 교대역, 양재역);

        // then
        경로_조회_응답_검증(response, 30, 4, 1650, 교대역, 강남역, 양재역);
    }

    @DisplayName("4개의 역을 가야하지만 소요시간이 10분 경로와, 3개의 역을 가지만 소요시간이 20분인 경우 최소 시간 단위 경로 조회")
    @Test
    void findPathByDurationBetweenDifferentSection() {
        // given
        Long 노선A = 지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 5, 5);
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철B역, 지하철C역, 5, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철C역, 지하철D역, 5, 5));

        Long 노선B = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 7, 10);
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선B, createSectionCreateParams(지하철E역, 지하철D역, 7, 10));

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(), 지하철A역, 지하철D역);

        // then
        경로_조회_응답_검증(response, 15, 15, 1350, 지하철A역, 지하철B역, 지하철C역, 지하철D역);
    }

    @DisplayName("4개의 역을 가야하지만 소요시간이 10분 경로와 4개의 역을 가야하지만 15분인 경우 최소 시간 단위 경로 조회")
    @Test
    void findPathByDurationBetweenDifferentDuration() {
        // given
        Long 노선A = 지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 2, 5);
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철B역, 지하철C역, 3, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선A, createSectionCreateParams(지하철C역, 지하철D역, 3, 5));

        Long 노선B = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 2, 3);
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선B, createSectionCreateParams(지하철E역, 지하철F역, 3, 3));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선B, createSectionCreateParams(지하철F역, 지하철D역, 3, 4));

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(), 지하철A역, 지하철D역);

        // then
        경로_조회_응답_검증(response, 8, 10, 1250, 지하철A역, 지하철E역, 지하철F역, 지하철D역);
    }

    @DisplayName("4개의 역을 환승을 두 번하고 20분 경로와 환승 없이 7개의 역을 25분경로 최소 시간 단위 경로 조회")
    @Test
    void findPathByDurationBetween() {
        // given
        지하철_노선_생성_요청("노선A", "white", 지하철A역, 지하철B역, 25, 8);
        지하철_노선_생성_요청("노선B", "black", 지하철B역, 지하철C역, 20, 6);
        지하철_노선_생성_요청("노선C", "red", 지하철C역, 지하철D역, 20, 6);

        Long 노선D = 지하철_노선_생성_요청("노선B", "black", 지하철A역, 지하철E역, 10, 5);
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철E역, 지하철F역, 10, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철F역, 지하철G역, 8, 4));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철G역, 지하철H역, 8, 4));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철H역, 지하철I역, 6, 4));
        지하철_노선에_지하철_구간_생성_요청(관리자, 노선D, createSectionCreateParams(지하철I역, 지하철D역, 4, 3));

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(), 지하철A역, 지하철D역);

        // then
        경로_조회_응답_검증(response, 65, 20, 2250, 지하철A역, 지하철B역, 지하철C역, 지하철D역);
    }

    @DisplayName("등록되지 않은 지하철역을 경로 조회 요청하면 예외 발생")
    @Test
    void findPathByDurationWithUnknownStation() {
        // given
        Long 등록되지_않은_지하철역 = 1234L;

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(given(), 등록되지_않은_지하철역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이어지지 않은 두 역의 경로를 조회하면 예외 발생")
    @Test
    void findPathByDistanceWithoutConnection() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(given(), 서울역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
