package nextstep.documentation.auth;

import static org.mockito.Mockito.when;

import static nextstep.api.auth.acceptance.AuthSteps.github_로그인_요청;
import static nextstep.api.auth.acceptance.AuthSteps.일반_로그인_요청;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;

import nextstep.api.auth.application.token.TokenService;
import nextstep.api.auth.application.token.dto.TokenResponse;
import nextstep.documentation.Documentation;

@ExtendWith(RestDocumentationExtension.class)
class AuthDocumentation extends Documentation {
    @MockBean
    private TokenService tokenService;

    @Test
    void createToken() {
        final var email = "user@email.com";
        final var password = "password";

        when(tokenService.createToken(email, password)).thenReturn(new TokenResponse("accessToken"));

        일반_로그인_요청(email, password, makeRequestSpec("auth-login"));
    }

    @Test
    void createTokenByGithub() {
        final var code = "code";

        when(tokenService.createTokenFromGithub(code)).thenReturn(new TokenResponse("accessToken"));

        github_로그인_요청(code, makeRequestSpec("auth-github-login"));
    }
}
