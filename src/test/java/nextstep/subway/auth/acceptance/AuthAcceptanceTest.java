package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.acceptance.MemberRequestSteps.*;
import static nextstep.subway.member.acceptance.MemberVerificationSteps.회원_정보_조회_됨;
import static nextstep.subway.utils.BaseDocumentation.givenDefault;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("Session 로그인 후 내 정보 조회")
    void myInfoWithSession() {
        회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(ADULT_EMAIL, PASSWORD);

        회원_정보_조회_됨(response, ADULT_EMAIL, ADULT_AGE);
    }

    @Test
    @DisplayName("Bearer Auth")
    void myInfoWithBearerAuth() {
        회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(givenDefault(), tokenResponse);

        회원_정보_조회_됨(response, ADULT_EMAIL, ADULT_AGE);
    }
}
