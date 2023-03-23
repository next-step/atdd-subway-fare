package nextstep.subway.documentation.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static io.restassured.RestAssured.given;
import static nextstep.subway.acceptance.support.PathSteps.경로_찾기_요청_데이터_생성;
import static nextstep.subway.documentation.Documentation.restDocsFilter;
import static nextstep.subway.fixture.FieldFixture.경로_내_역_아이디_목록;
import static nextstep.subway.fixture.FieldFixture.경로_내_역_이름_목록;
import static nextstep.subway.fixture.FieldFixture.경로_요금;
import static nextstep.subway.fixture.FieldFixture.경로_조회_도착지_아이디;
import static nextstep.subway.fixture.FieldFixture.경로_조회_출발지_아이디;
import static nextstep.subway.fixture.FieldFixture.경로_조회_타입;
import static nextstep.subway.fixture.FieldFixture.구간_거리;
import static nextstep.subway.fixture.FieldFixture.구간_소요시간;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentSupport {

    public static ExtractableResponse<Response> 지하철_경로_조회_요청(RequestSpecification spec, long 출발역_id, long 도착역_id, String 타입) {
        return given(spec).log().all()
                .filter(restDocsFilter("path", 경로_조회_파라미터(), 경로_조회_응답_필드()))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(경로_찾기_요청_데이터_생성(출발역_id, 도착역_id, 타입))
                .when().get("/paths")
                .then().log().all().extract();
    }

    private static RequestParametersSnippet 경로_조회_파라미터() {
        return requestParameters(
                parameterWithName(경로_조회_출발지_아이디.문서_필드명()).description(경로_조회_출발지_아이디.필드_설명()),
                parameterWithName(경로_조회_도착지_아이디.문서_필드명()).description(경로_조회_도착지_아이디.필드_설명()),
                parameterWithName(경로_조회_타입.문서_필드명()).description(경로_조회_타입.필드_설명())
        );
    }

    private static ResponseFieldsSnippet 경로_조회_응답_필드() {
        return responseFields(
                fieldWithPath(경로_내_역_아이디_목록.문서_필드명()).type(JsonFieldType.NUMBER).description(경로_내_역_아이디_목록.필드_설명()),
                fieldWithPath(경로_내_역_이름_목록.문서_필드명()).type(JsonFieldType.STRING).description(경로_내_역_이름_목록.필드_설명()),
                fieldWithPath(구간_거리.문서_필드명()).type(JsonFieldType.NUMBER).description(구간_거리.필드_설명()),
                fieldWithPath(구간_소요시간.문서_필드명()).type(JsonFieldType.NUMBER).description(구간_소요시간.필드_설명()),
                fieldWithPath(경로_요금.문서_필드명()).type(JsonFieldType.NUMBER).description(경로_요금.필드_설명())
        );
    }
}
