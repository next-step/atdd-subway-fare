package nextstep.api.acceptance.auth;

import static nextstep.api.acceptance.AcceptanceHelper.asResponse;
import static nextstep.api.acceptance.AcceptanceHelper.statusCodeShouldBe;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.auth.application.token.dto.TokenRequest;
import nextstep.api.auth.application.token.dto.TokenResponse;

public class AuthSteps {
    public static ValidatableResponse 일반_로그인_요청(
            final String email, final String password, final RequestSpecification requestSpecification
    ) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(email, password))
                .when().post("/login/token")
                .then();
    }

    public static ValidatableResponse 일반_로그인_요청(final String email, final String password) {
        return 일반_로그인_요청(email, password, RestAssured.given());
    }

    public static TokenResponse 일반_로그인_성공(final String email, final String password) {
        final var response = 일반_로그인_요청(email, password);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, TokenResponse.class);
    }

    public static void 일반_로그인_실패(final String email, final String password, final HttpStatus httpStatus) {
        final var response = 일반_로그인_요청(email, password);
        statusCodeShouldBe(response, httpStatus);
    }

    public static ValidatableResponse github_로그인_요청(
            final String code, final RequestSpecification requestSpecification
    ) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("code", code))
                .when().post("/login/github")
                .then();
    }

    public static ValidatableResponse github_로그인_요청(final String code) {
        return github_로그인_요청(code, RestAssured.given());
    }

    public static TokenResponse github_로그인_성공(final String code) {
        final var response = github_로그인_요청(code);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, TokenResponse.class);
    }

    public static void github_로그인_실패(final String code, final HttpStatus httpStatus) {
        final var response = github_로그인_요청(code);
        statusCodeShouldBe(response, httpStatus);
    }
}
