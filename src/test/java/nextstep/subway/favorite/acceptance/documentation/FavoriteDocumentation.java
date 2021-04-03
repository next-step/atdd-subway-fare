package nextstep.subway.favorite.acceptance.documentation;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.utils.BaseDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class FavoriteDocumentation extends BaseDocumentation {

    public FavoriteDocumentation(RequestSpecification spec) {
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
                fieldWithPath("sourceId").type(JsonFieldType.NUMBER).description("출발역 ID"),
                fieldWithPath("targetId").type(JsonFieldType.NUMBER).description("도착역 ID")
        );
    }

    @Override
    public ResponseFieldsSnippet initDocumentResponseBody() {
        return responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("즐겨찾기 ID"),
                fieldWithPath("source.id").type(JsonFieldType.NUMBER).description("출발역 ID"),
                fieldWithPath("source.name").type(JsonFieldType.STRING).description("출발역 이름"),
                fieldWithPath("source.createdDate").type(JsonFieldType.STRING).description("출발역 등록 날짜"),
                fieldWithPath("source.modifiedDate").type(JsonFieldType.STRING).description("출발역 최종수정 날짜"),
                fieldWithPath("target.id").type(JsonFieldType.NUMBER).description("도착역 ID"),
                fieldWithPath("target.name").type(JsonFieldType.STRING).description("도착역 이름"),
                fieldWithPath("target.createdDate").type(JsonFieldType.STRING).description("도착역 등록 날짜"),
                fieldWithPath("target.modifiedDate").type(JsonFieldType.STRING).description("도착역 최종수정 날짜")
        );
    }
}
