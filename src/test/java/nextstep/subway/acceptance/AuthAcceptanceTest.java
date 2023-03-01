package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.인증_로그인_응답_실패;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 기능 테스트")
class AuthAcceptanceTest extends AcceptanceTest {

    /**
     * Given 회원을 등록하고
     * When 등록한 화원이 로그인 요청을 하면
     * Then 토큰을 응답 받는다.
     */
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * Given 회원을 등록하고
     * When 등록한 화원이 로그인 요청 시 이메일을 잘못 입력하면
     * Then 로그인의 실패한다.
     */
    @DisplayName("패스워드를 잘못 입력해서 Bearer Auth 인증에 실패한다.")
    @Test
    void error_bearerAuth() {

        회원_생성_요청(EMAIL, PASSWORD, 10);

        final ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, "different password");

        인증_로그인_응답_실패(response);
    }

    /**
     * Given 회원을 등록하고
     * When 등록한 화원이 깃허브 로그인 요청을 하면
     * Then 토큰을 응답 받는다.
     */
    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(GithubResponses.사용자1.getCode());

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}