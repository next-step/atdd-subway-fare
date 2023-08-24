package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.TokenResponse;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {


    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        var response = 베어러_인증_로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        assertThat(response.getAccessToken()).isNotBlank();
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        var response = 깃허브_인증_로그인_요청(GithubResponses.사용자1.getCode()).as(TokenResponse.class);

        assertThat(response.getAccessToken()).isNotBlank();
    }
}