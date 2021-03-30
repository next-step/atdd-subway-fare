package nextstep.subway.member;

import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static nextstep.subway.utils.AcceptanceTest.givenAndCreateDocumentForFields;
import static nextstep.subway.utils.AcceptanceTest.givenAndCreateDocumentForParameters;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class MemberDocumentSteps {

    private static final String DOCUMENT_IDENTIFIER_MEMBER = "member/{method-name}";

    public static RequestSpecification 사용자_회원가입_문서화_요청() {
        return givenAndCreateDocumentForFields(DOCUMENT_IDENTIFIER_MEMBER, getMemberDocumentCreateMemberRequestBody(), getMemberDocumentCreateMemberResponseBody());
    }

    private static RequestFieldsSnippet getMemberDocumentCreateMemberRequestBody() {
        return requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이")
        );
    }

    private static ResponseFieldsSnippet getMemberDocumentCreateMemberResponseBody() {
        return responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("로그인 사용자 ID"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 사용자 이메일"),
                fieldWithPath("age").type(JsonFieldType.NUMBER).description("로그인 사용자 나이")
        );
    }

    public static RequestSpecification 사용자_로그인_문서화_요청() {
        return givenAndCreateDocumentForFields(DOCUMENT_IDENTIFIER_MEMBER, getMemberDocumentLoginMemberRequestBody(), getMemberDocumentLoginMemberResponseBody());
    }

    private static RequestFieldsSnippet getMemberDocumentLoginMemberRequestBody() {
        return requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
        );
    }

    private static ResponseFieldsSnippet getMemberDocumentLoginMemberResponseBody() {
        return responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("로그인 사용자 AccessToken")
        );
    }

    public static RequestSpecification 회원_정보_조회_문서화_요청() {
        return givenAndCreateDocumentForParameters(DOCUMENT_IDENTIFIER_MEMBER, requestParameters(), getMemberDocumentFindMemberOfMineResponseBody());
    }

    private static ResponseFieldsSnippet getMemberDocumentFindMemberOfMineResponseBody() {
        return responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("로그인 사용자 ID"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 사용자 이메일"),
                fieldWithPath("age").type(JsonFieldType.NUMBER).description("로그인 사용자 나이")
        );
    }
}
