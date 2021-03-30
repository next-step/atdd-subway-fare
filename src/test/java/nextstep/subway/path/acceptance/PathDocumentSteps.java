package nextstep.subway.path.acceptance;

import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static nextstep.subway.utils.AcceptanceTest.givenAndCreateDocumentForParameters;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentSteps {

    private static final String DOCUMENT_IDENTIFIER_PATH = "path/{method-name}";

    public static RequestSpecification 최단_경로_탐색_문서화_요청() {
        return givenAndCreateDocumentForParameters(DOCUMENT_IDENTIFIER_PATH, getPathDocumentRequestParameters(), getPathDocumentResponseFields());
    }

    public static RequestParametersSnippet getPathDocumentRequestParameters() {
        return requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("조회 방법")
        );
    }

    public static ResponseFieldsSnippet getPathDocumentResponseFields() {
        return responseFields(
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("탐색 경로 역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("역 등록 날짜"),
                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("역 최종수정 날짜"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리 (km)"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간 (분)"),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
        );
    }
}
