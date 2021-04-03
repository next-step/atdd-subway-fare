package nextstep.subway.path.acceptance.documentation;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.utils.BaseDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentation extends BaseDocumentation {

    public PathDocumentation(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public PathParametersSnippet initDocumentRequestPathVariable() {
        return null;
    }

    @Override
    public RequestParametersSnippet initDocumentRequestParameters() {
        return requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("조회 방법")
        );
    }

    @Override
    public RequestFieldsSnippet initDocumentRequestBody() {
        return null;
    }

    @Override
    public ResponseFieldsSnippet initDocumentResponseBody() {
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
