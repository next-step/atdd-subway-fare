package nextstep.subway.station.acceptance.documentation;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.utils.BaseDocumentSteps;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class StationDocumentation extends BaseDocumentSteps {

    public StationDocumentation(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public PathParametersSnippet initDocumentRequestPathVariable() {
        return pathParameters();
    }

    @Override
    public RequestParametersSnippet initDocumentRequestParameters() {
        return requestParameters();
    }

    @Override
    public RequestFieldsSnippet initDocumentRequestBody() {
        return requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")
        );
    }

    @Override
    public ResponseFieldsSnippet initDocumentResponseBody() {
        return responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("역 ID"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름"),
                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("역 등록 날짜"),
                fieldWithPath("modifiedDate").type(JsonFieldType.STRING).description("역 최종수정 날짜")
        );
    }
}
