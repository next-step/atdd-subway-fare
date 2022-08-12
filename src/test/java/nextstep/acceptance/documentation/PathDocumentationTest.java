package nextstep.acceptance.documentation;

import io.restassured.specification.RequestSpecification;
import nextstep.path.domain.PathSearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static nextstep.acceptance.steps.LineSectionSteps.*;
import static nextstep.acceptance.steps.PathSteps.경로_조회_정보가_일치한다;
import static nextstep.acceptance.steps.PathSteps.경로를_조회한다;
import static nextstep.acceptance.steps.StationSteps.지하철역_생성_요청;
import static nextstep.acceptance.steps.DocumentationTestSteps.givenDocs;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@DisplayName("지하철 경로 조회 문서화")
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


    @DisplayName("경로 조회 API 문서화")
    @Test
    void path() {
        RequestSpecification 문서_정보 = givenDocs(
                spec,
                "path",
                requestParameters(
                        parameterWithName("source").description("출발역 id"),
                        parameterWithName("target").description("도착역 id"),
                        parameterWithName("type").description("경로 조회 타입: DURATION(최소 시간), DISTANCE(최소 거리)")
                ),
                responseFields(
                        fieldWithPath("stations[].id").description("경로 조회 결과 역 id"),
                        fieldWithPath("stations[].name").description("경로 조회 결과 역 이름"),
                        fieldWithPath("distance").description("총 거리"),
                        fieldWithPath("duration").description("총 시간"),
                        fieldWithPath("fare").description("총 요금")
                )
        );

        // when
        var response = 경로를_조회한다(남부터미널역, 강남역, PathSearchType.DISTANCE, 문서_정보);

        // then
        경로_조회_정보가_일치한다(response, 13, 12, 1350, 남부터미널역, 양재역, 강남역);
    }
}
