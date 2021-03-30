package nextstep.subway.line.acceptance.linesection;

import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static nextstep.subway.line.acceptance.line.LineDocumentSteps.getLineDocumentCreateLineResponseBody;
import static nextstep.subway.utils.AcceptanceTest.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class LineSectionDocumentSteps {

    private static final String DOCUMENT_IDENTIFIER_LINE_SECTION = "linesection/{method-name}";

    public static RequestSpecification 지하철_노선_구간_생성_문서화_요청() {
        return givenAndCreateDocumentForAllRequestType(DOCUMENT_IDENTIFIER_LINE_SECTION, getLineSectionDocumentLinePathVariable(), getLineSectionDocumentSectionRequestBody(), getLineDocumentCreateLineResponseBody());
    }

    private static PathParametersSnippet getLineSectionDocumentLinePathVariable() {
        return pathParameters(
                parameterWithName("lineId").description("노선 ID")
        );
    }

    private static RequestFieldsSnippet getLineSectionDocumentSectionRequestBody() {
        return requestFields(
                fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("상행역 ID"),
                fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("하행역 ID"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("구간 거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("구간 소요 시간")
        );
    }

    public static RequestSpecification 지하철_노선_구간_제거_문서화_요청() {
        return givenAndCreateDocumentForPathAndParameters(DOCUMENT_IDENTIFIER_LINE_SECTION, getLineSectionDocumentLinePathVariable(), getLineSectionDocumentDeleteStationPathVariable());
    }

    private static RequestParametersSnippet getLineSectionDocumentDeleteStationPathVariable() {
        return requestParameters(
                parameterWithName("stationId").description("삭제할 지하철 역 ID")
        );
    }
}
