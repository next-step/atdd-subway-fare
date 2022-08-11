package nextstep.documentation;

import io.restassured.RestAssured;
import nextstep.path.domain.PathSearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static nextstep.acceptance.steps.LineSectionSteps.*;
import static nextstep.acceptance.steps.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

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
        var response = RestAssured
                .given(spec).log().all()
                .filter(document(
                        "path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 id"),
                                parameterWithName("target").description("도착역 id"),
                                parameterWithName("type").description("경로 조회 타입 (DURATION/DISTANCE)"))
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 남부터미널역)
                .queryParam("target", 강남역)
                .queryParam("type", PathSearchType.DISTANCE.name())
                .when().get("/paths")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(13);
        assertThat(response.jsonPath().getList("stations.id", Long.class))
                .containsExactly(남부터미널역, 양재역, 강남역);
    }
    
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void pathByDuration() {
        var response = RestAssured
                .given(spec).log().all()
                .filter(document(
                        "path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 id"),
                                parameterWithName("target").description("도착역 id"),
                                parameterWithName("type").description("경로 조회 타입 (DURATION/DISTANCE)"))
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 남부터미널역)
                .queryParam("target", 강남역)
                .queryParam("type", PathSearchType.DURATION.name())
                .when().get("/paths")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(12);
        assertThat(response.jsonPath().getList("stations.id", Long.class))
                .containsExactly(남부터미널역, 교대역, 강남역);
    }

}
