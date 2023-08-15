package nextstep.api.documentation.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import static nextstep.api.acceptance.auth.AuthSteps.github_로그인_요청;
import static nextstep.api.acceptance.auth.AuthSteps.일반_로그인_요청;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.auth.application.token.TokenService;
import nextstep.api.auth.application.token.dto.TokenResponse;
import nextstep.api.documentation.Documentation;

class AuthDocumentation extends Documentation {
    @MockBean
    private TokenService tokenService;

    private final TokenResponse response = new TokenResponse("accessToken");

    @Test
    void createToken() {
        when(tokenService.createToken(any(), any())).thenReturn(response);

        일반_로그인_요청("user@email.com", "password", makeRequestSpec(
                document("auth-login",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("요청 헤더에 사용될 AccessToken")
                        )
                ))
        );
    }

    @Test
    void createTokenByGithub() {
        when(tokenService.createTokenFromGithub(any())).thenReturn(response);

        github_로그인_요청("code", makeRequestSpec(
                document("auth-github-login",
                        requestFields(
                                fieldWithPath("code").description("github code")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("요청 헤더에 사용될 AccessToken")
                        )
                ))
        );
    }
}
