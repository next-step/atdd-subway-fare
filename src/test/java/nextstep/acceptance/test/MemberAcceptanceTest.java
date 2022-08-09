package nextstep.acceptance.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.acceptance.steps.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var createResponse = 회원_생성_요청(createMemberParams(EMAIL, PASSWORD, AGE));

        // then
        회원가입에_성공한다(createResponse);
    }

    @DisplayName("중복 이메일로 회원가입 할 수 없다.")
    @Test
    void createMember_Exception() {
        // when
        Map<String, String> params = createMemberParams(EMAIL, PASSWORD, AGE);
        회원_생성_요청(params);
        var duplicateCreateResponse = 회원_생성_요청(params);

        // then
        회원가입에_실패한다(duplicateCreateResponse);
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        회원_생성_요청(createMemberParams(EMAIL, PASSWORD, AGE));
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        var response = 회원_정보_조회_요청(accessToken);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("내 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        회원_생성_요청(createMemberParams(EMAIL, PASSWORD, AGE));
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        String updateEmail = "update@email.com";

        // when
        회원_정보_수정_요청(accessToken, createMemberParams(updateEmail, PASSWORD, AGE));
        String updateToken = 로그인_되어_있음(updateEmail, PASSWORD);

        // then
        var response = 회원_정보_조회_요청(updateToken);
        회원_정보_조회됨(response, updateEmail, AGE);
    }

    @DisplayName("내 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        회원_생성_요청(createMemberParams(EMAIL, PASSWORD, AGE));
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        var deleteResponse = 회원_삭제_요청(accessToken);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        내_정보가_삭제된다(accessToken);
    }

    private Map<String, String> createMemberParams(String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");
        return params;
    }

    private void 회원가입에_성공한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 회원가입에_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 내_정보가_삭제된다(String accessToken) {
        var response = 회원_정보_조회_요청(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}