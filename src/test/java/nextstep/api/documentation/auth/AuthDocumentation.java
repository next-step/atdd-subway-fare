package nextstep.api.documentation.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

        일반_로그인_요청("user@email.com", "password", makeRequestSpec("auth-login"));
    }

    @Test
    void createTokenByGithub() {
        when(tokenService.createTokenFromGithub(any())).thenReturn(response);

        github_로그인_요청("code", makeRequestSpec("auth-github-login"));
    }
}
