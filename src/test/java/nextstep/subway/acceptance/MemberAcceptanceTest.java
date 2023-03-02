package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 기능 테스트")
class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String ADMIN = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    /**
     * When 회원가입 생성 요청을 하면
     * Then 회원가입이 정상적으로 등록된다
     */
    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 회원가입 생성 요청을 하고
     * When 생성한 회원 조회 요청을 하면
     * Then 생성한 회원의 정보를 응답받는다.
     */
    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
     * Given 회원가입 생성 요청을 하고
     * When 생성한 회원의 정보 수정 요청을 하면
     * Then 회원 정보가 수정된다.
     */
    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 회원가입 생성 요청을 하고
     * When 생성한 회원의 정보 삭제 요청을 하면
     * Then 회원이 삭제된다.
     */
    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 등록한 회원이 로그인 요청을 하고
     * When 자신의 정보를 조회 요청하면
     * Then 자신의 정보가 조회된다.
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        String accessToken = 베어러_인증_로그인_요청(ADMIN, PASSWORD).jsonPath().getString("accessToken");

        ExtractableResponse<Response> response = 회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, ADMIN, AGE);
    }
}