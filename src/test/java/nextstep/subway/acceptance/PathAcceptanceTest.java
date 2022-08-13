package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선(10km, 5분)* ---   강남역
     * |                                      |
     * *3호선(2km, 4분)*                   *신분당선(11km, 3분)*
     * |                                      |
     * 남부터미널역  --- *3호선(3km, 8분)* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 5);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 11, 3);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 4);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 8));
    }

    @Nested
    @DisplayName("성공")
    class success {
        @DisplayName("두 역의 최단 거리 경로를 조회한다.")
        @Test
        void findPathByDistance() {
            // when
            ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE");

            // then
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
            assertThat(response.jsonPath().getLong("distance")).isEqualTo(5);
            assertThat(response.jsonPath().getLong("duration")).isEqualTo(12);
            assertThat(response.jsonPath().getLong("fare")).isEqualTo(1250);
        }

        @DisplayName("두 역의 최소 시간 경로를 조회한다.")
        @Test
        void findPathByDuration() {
            // when
            ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DURATION");

            // then
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
            assertThat(response.jsonPath().getLong("distance")).isEqualTo(21);
            assertThat(response.jsonPath().getLong("duration")).isEqualTo(8);
            assertThat(response.jsonPath().getLong("fare")).isEqualTo(1250);
        }
    }

    @Nested
    @DisplayName("실패")
    class fail {
        @DisplayName("출발역이 노선에 없을 경우 조회 실패")
        @Test
        void findNotExistsStartStation() {
            long 출발역 = 지하철역_생성_요청(관리자, "출발역").jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> responseByDistance = 두_역의_경로_조회를_요청(출발역, 강남역, "DISTANCE");
            ExtractableResponse<Response> responseByDuration = 두_역의_경로_조회를_요청(출발역, 강남역, "DURATION");

            // then
            assertThat(responseByDistance.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(responseByDuration.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("도착역이 노선에 없을 경우 조회 실패")
        @Test
        void findNotExistsEndStation() {
            long 도착역 = 지하철역_생성_요청(관리자, "도착역").jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> responseByDistance = 두_역의_경로_조회를_요청(강남역, 도착역, "DISTANCE");
            ExtractableResponse<Response> responseByDuration = 두_역의_경로_조회를_요청(강남역, 도착역, "DURATION");

            // then
            assertThat(responseByDistance.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(responseByDuration.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("출발역과 도착역이 노선에 없을 경우 조회 실패")
        @Test
        void findNotExistsEndStationAndStartStation() {
            long 출발역 = 지하철역_생성_요청(관리자, "출발역").jsonPath().getLong("id");
            long 도착역 = 지하철역_생성_요청(관리자, "도착역").jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> responseByDistance = 두_역의_경로_조회를_요청(출발역, 도착역, "DISTANCE");
            ExtractableResponse<Response> responseByDuration = 두_역의_경로_조회를_요청(출발역, 도착역, "DURATION");

            // then
            assertThat(responseByDistance.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(responseByDuration.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
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
