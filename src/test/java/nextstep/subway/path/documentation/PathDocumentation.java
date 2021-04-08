package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import nextstep.subway.Documentation;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.acceptance.PathSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

public class PathDocumentation extends Documentation {

    private StationResponse 교대역;
    private StationResponse 강남역;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);

        지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10, 10);
    }

    @Test
    void path() {
        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        getPathRequestParamDescription(),
                        getPathResponseFieldDescription()))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 교대역.getId())
                .queryParam("target", 강남역.getId())
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }

    private RequestParametersSnippet getPathRequestParamDescription() {
        return requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("검색 방법 (DISTANCE|DURATION)"));
    }

    private ResponseFieldsSnippet getPathResponseFieldDescription() {
        return responseFields(
                fieldWithPath("stations").description("역정보 목록"),
                fieldWithPath("stations[].id").description("ID"),
                fieldWithPath("stations[].name").description("이름"),
                fieldWithPath("stations[].createdDate").description("생성 시간"),
                fieldWithPath("stations[].modifiedDate").description("변경 시간"),
                fieldWithPath("distance").description("역간 거리"),
                fieldWithPath("duration").description("역간 소요시간"),
                fieldWithPath("fare").description("이용 요금"));
    }

}

