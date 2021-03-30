package nextstep.subway.line.acceptance.line;

import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;

import static nextstep.subway.utils.AcceptanceTest.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class LineDocumentSteps {

    private static final String DOCUMENT_IDENTIFIER_LINE = "line/{method-name}";

    public static RequestSpecification 지하철_노선_생성_문서화_요청() {
        return givenAndCreateDocumentForFields(DOCUMENT_IDENTIFIER_LINE, getLineDocumentCreateLineRequestBody(), getLineDocumentCreateLineResponseBody());
    }

    private static RequestFieldsSnippet getLineDocumentCreateLineRequestBody() {
        return requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색상"),
                fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("노선 상행역 ID"),
                fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("노선 하행역 ID"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("노선 거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("노선 소요시간")
        );
    }

    public static ResponseFieldsSnippet getLineDocumentCreateLineResponseBody() {
        return responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("노선 ID"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색상"),
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("노선에 포함된 역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("노선에 포함된 역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("노선에 포함된 역 이름"),
                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("노선에 포함된 역 등록 날짜"),
                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("노선에 포함된 역 최종수정 날짜"),
                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("노선 등록 날짜"),
                fieldWithPath("modifiedDate").type(JsonFieldType.STRING).description("노선 최종수정 날짜")
        );
    }

    public static RequestSpecification 지하철_노선_목록_조회_문서화_요청() {
        return givenAndCreateDocumentForEmptyRequest(DOCUMENT_IDENTIFIER_LINE);
    }

    public static RequestSpecification 지하철_노선_조회_문서화_요청() {
        return givenAndCreateDocumentForPathVariables(DOCUMENT_IDENTIFIER_LINE, getLineDocumentFindLineRequestPathVariable(), getLineDocumentCreateLineResponseBody());
    }

    private static PathParametersSnippet getLineDocumentFindLineRequestPathVariable() {
        return pathParameters(
                parameterWithName("lineId").description("지하철 노선 ID")
        );
    }

    public static RequestSpecification 지하철_노선_수정_문서화_요청() {
        return givenAndCreateDocumentForEmptyRequest(DOCUMENT_IDENTIFIER_LINE);
    }

    public static RequestSpecification 지하철_노선_제거_문서화_요청() {
        return givenAndCreateDocumentForPathVariable(DOCUMENT_IDENTIFIER_LINE, getLineDocumentFindLineRequestPathVariable());
    }
}
