package nextstep.acceptance.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.acceptance.steps.AuthSteps.*;
import static nextstep.acceptance.steps.LineSectionSteps.지하철_노선_생성_요청;
import static nextstep.acceptance.steps.MemberSteps.로그인_되어_있음;
import static nextstep.acceptance.steps.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "member@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        var response = 베이직_인증으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        var response = 폼_로그인_후_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        var response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Unauthorized")
    @Test
    void unAuthorized() {
        var response = 로그인_요청("wrong@wrong.com", "wrongwrong");

        인증_실패(response);
    }

    @DisplayName("Forbidden")
    @Test
    void forbidden() {
        String 사용자 = 로그인_되어_있음(EMAIL, PASSWORD);

        var response = 지하철_노선_생성_요청(사용자, "2호선", "green");

        권한이_없다(response);
    }

    private void 인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 권한이_없다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
