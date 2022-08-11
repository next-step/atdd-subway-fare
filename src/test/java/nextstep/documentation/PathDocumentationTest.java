package nextstep.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static nextstep.acceptance.steps.AcceptanceTestSteps.given;
import static nextstep.acceptance.steps.AcceptanceTestSteps.givenDocs;
import static nextstep.acceptance.steps.LineSectionSteps.*;
import static nextstep.acceptance.steps.StationSteps.지하철역_생성_요청;
import static nextstep.documentation.PathSteps.최소_거리_경로를_조회한다;
import static nextstep.documentation.PathSteps.최소_시간_경로를_조회한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@DisplayName("지하철 경로 검색 with 문서화")
class PathDocumentationTest extends DocumentationTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 1. 거리 기준
     *
     * 교대역    --- *2호선(6)* ---     강남역
     * |                              |
     * *3호선(8)*                   *신분당선(10)*
     * |                              |
     * 남부터미널역  --- *3호선(3)* ---   양재역
     *
     * 2. 소요시간 기준
     *
     * 교대역    --- *2호선(8)* ---     강남역
     * |                              |
     * *3호선(3)*                   *신분당선(5)*
     * |                              |
     * 남부터미널역  --- *3호선(7)* ---   양재역
     */
    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청(관리자, createLineCreateParams("2호선", "green", 교대역, 강남역, 6, 8)).jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청(관리자, createLineCreateParams("신분당선", "red", 강남역, 양재역, 10, 5)).jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청(관리자, createLineCreateParams("3호선", "orange", 교대역, 남부터미널역, 8, 3)).jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 7));
    }


    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void pathByDistance() {
        RequestSpecification 문서_정보 = givenDocs(
                spec,
                "path",
                requestParameters(
                        parameterWithName("source").description("출발역 id"),
                        parameterWithName("target").description("도착역 id"),
                        parameterWithName("type").description("경로 조회 타입 (DURATION/DISTANCE)")
                ),
                responseFields(
                        fieldWithPath("stations[].id").description("경로 조회 결과 역id"),
                        fieldWithPath("stations[].name").description("경로 조회 결과 역이름"),
                        fieldWithPath("distance").description("총 거리"),
                        fieldWithPath("duration").description("총 소요시간")
                )
        );

        // when
        var response = 최소_거리_경로를_조회한다(문서_정보, 남부터미널역, 강남역);

        // then
        경로_조회_정보가_일치한다(response, 13, 12, 남부터미널역, 양재역, 강남역);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void pathByDuration() {
        // when
        var response = 최소_시간_경로를_조회한다(given(), 남부터미널역, 강남역);

        // then
        경로_조회_정보가_일치한다(response, 14, 11, 남부터미널역, 교대역, 강남역);
    }

    private void 경로_조회_정보가_일치한다(ExtractableResponse<Response> response, int distance, int duration, Long... stationIds) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
    }
}
