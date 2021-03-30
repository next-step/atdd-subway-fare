package nextstep.subway.favorite.acceptance;

import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;

import static nextstep.subway.utils.AcceptanceTest.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class FavoriteDocumentSteps {

    private static final String DOCUMENT_IDENTIFIER_FAVORITE = "favorite/{method-name}";

    public static RequestSpecification 즐겨찾기_추가_문서화_요청() {
        return givenAndCreateDocumentForFields(DOCUMENT_IDENTIFIER_FAVORITE, getFavoriteDocumentFavoriteRequestBody(), getFavoriteDocumentFavoriteResponseBody());
    }

    private static RequestFieldsSnippet getFavoriteDocumentFavoriteRequestBody() {
        return requestFields(
            fieldWithPath("sourceId").type(JsonFieldType.NUMBER).description("출발역 ID"),
            fieldWithPath("targetId").type(JsonFieldType.NUMBER).description("도착역 ID")
        );
    }

    private static ResponseFieldsSnippet getFavoriteDocumentFavoriteResponseBody() {
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

    public static RequestSpecification 즐겨찾기_조회_문서화_요청() {
        return givenAndCreateDocumentForEmptyRequest(DOCUMENT_IDENTIFIER_FAVORITE);
    }

    private static PathParametersSnippet getFavoriteDocumentFindFavoriteRequestPathVariable() {
        return pathParameters(
                parameterWithName("favoriteId").description("즐겨찾기 ID")
        );
    }

    public static RequestSpecification 즐겨찾기_제거_문서화_요청() {
        return givenAndCreateDocumentForPathVariable(DOCUMENT_IDENTIFIER_FAVORITE, getFavoriteDocumentFindFavoriteRequestPathVariable());
    }
}
