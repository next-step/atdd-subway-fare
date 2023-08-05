package nextstep.api.auth.acceptance;

import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class AuthSteps {
    public static ValidatableResponse 일반_로그인_요청(final String email, final String password,
                                                final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of(
                        "email", email,
                        "password", password
                ))
                .when().post("/login/token")
                .then();
    }

    public static ValidatableResponse 일반_로그인_요청(final String email, final String password) {
        return 일반_로그인_요청(email, password, RestAssured.given());
    }

    public static ValidatableResponse github_로그인_요청(final String code,
                                                    final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("code", code))
                .when().post("/login/github")
                .then();
    }

    public static ValidatableResponse github_로그인_요청(final String code) {
        return github_로그인_요청(code, RestAssured.given());
    }
}
