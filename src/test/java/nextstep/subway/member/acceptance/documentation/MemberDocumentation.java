package nextstep.subway.member.acceptance.documentation;

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

public class MemberDocumentation extends BaseDocumentation {

    public MemberDocumentation(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public PathParametersSnippet initDocumentRequestPathVariable() {
        return pathParameters();
    }

    @Override
    public RequestFieldsSnippet initDocumentRequestBody() {
        return requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이")
        );
    }

    @Override
    public RequestParametersSnippet initDocumentRequestParameters() {
        return requestParameters();
    }

    @Override
    public ResponseFieldsSnippet initDocumentResponseBody() {
        return responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("로그인 사용자 ID"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 사용자 이메일"),
                fieldWithPath("age").type(JsonFieldType.NUMBER).description("로그인 사용자 나이")
        );
    }
}
