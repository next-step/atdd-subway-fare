package nextstep.auth.acceptance;

import static nextstep.member.MemberSteps.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.AcceptanceTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class AuthAcceptanceTest extends AcceptanceTest {
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final Integer AGE = 20;

	@DisplayName("아이디 패스워드로 내 정보 조회")
	@Test
	void 아이디_패스워드로_내_정보_조회() {
		회원_생성_요청(EMAIL, PASSWORD, AGE);

		ExtractableResponse<Response> response = 내_회원_정보_조회_요청(EMAIL, PASSWORD);

		회원_정보_조회됨(response, EMAIL, AGE);
	}

	@DisplayName("Bearer Auth")
	@Test
	void 토큰으로_내_정보_조회() {
		회원_생성_요청(EMAIL, PASSWORD, AGE);

		String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

		ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);

		회원_정보_조회됨(response, EMAIL, AGE);
	}
}