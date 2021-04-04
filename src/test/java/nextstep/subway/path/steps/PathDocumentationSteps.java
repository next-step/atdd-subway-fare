package nextstep.subway.path.steps;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentationSteps {

    public static RestDocumentationFilter 문서화_필터_설정(String identifier, RequestParametersSnippet requestParametersSnippet, ResponseFieldsSnippet responseFieldsSnippet) {
        return document(identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParametersSnippet,
                responseFieldsSnippet);
    }

    public static RequestSpecification 문서화_요청_설정(RequestSpecification spec, RestDocumentationFilter documentationFilter) {
        return RestAssured.given(spec).log().all().filter(documentationFilter);
    }

    public static ResponseFieldsSnippet 경로조회_응답_설정() {
        return responseFields(
                fieldWithPath("stations[]").description("역 정보"),
                fieldWithPath("stations[].id").description("역 아이디"),
                fieldWithPath("stations[].name").description("역 이름"),
                fieldWithPath("stations[].createdDate").description("생성일시"),
                fieldWithPath("stations[].modifiedDate").description("수정일시"),
                fieldWithPath("distance").description("구간의 거리"),
                fieldWithPath("duration").description("구간의 소요시간"),
                fieldWithPath("fare").description("이용요금")
        );
    }

    public static RequestParametersSnippet 경로조회_요청_설정() {
        return requestParameters(
                parameterWithName("source").description("출발역 아이디"),
                parameterWithName("target").description("도착역 아이디"),
                parameterWithName("type").description("DISTANCE : 최소거리 / DURATION : 최소시간")
        );
    }
}
