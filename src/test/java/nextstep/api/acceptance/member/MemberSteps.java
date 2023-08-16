package nextstep.api.acceptance.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import static nextstep.api.acceptance.AcceptanceHelper.asResponse;
import static nextstep.api.acceptance.AcceptanceHelper.statusCodeShouldBe;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.member.application.dto.MemberRequest;
import nextstep.api.member.application.dto.MemberResponse;

public class MemberSteps {

    public static ValidatableResponse 회원_생성_요청(final String email, final String password, final Integer age,
                                               final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(email, password, age))
                .when().post("/members")
                .then();
    }

    public static ValidatableResponse 회원_생성_요청(final String email, final String password, final Integer age) {
        return 회원_생성_요청(email, password, age, RestAssured.given());
    }

    public static MemberResponse 회원_생성_성공(final String email, final String password, final Integer age) {
        final var response = 회원_생성_요청(email, password, age);
        statusCodeShouldBe(response, HttpStatus.CREATED);
        return asResponse(response, MemberResponse.class);
    }

    public static ValidatableResponse 회원_정보_조회_요청(final Long id, final RequestSpecification restAssured) {
        return restAssured
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", id)
                .when().get("/members/{id}")
                .then();
    }

    public static ValidatableResponse 회원_정보_조회_요청(final Long id) {
        return 회원_정보_조회_요청(id, RestAssured.given());
    }

    public static MemberResponse 회원_정보_조회_성공(final Long id) {
        final var response = 회원_정보_조회_요청(id);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, MemberResponse.class);
    }

    public static ValidatableResponse 내_정보_조회_요청(final String token, final RequestSpecification restAssured) {
        return restAssured
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then();
    }

    public static ValidatableResponse 내_정보_조회_요청(final String token) {
        return 내_정보_조회_요청(token, RestAssured.given());
    }

    public static MemberResponse 내_정보_조회_성공(final String token) {
        final var response = 내_정보_조회_요청(token);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, MemberResponse.class);
    }

    public static ValidatableResponse 회원_정보_수정_요청(
            final Long id, final String email, final String password, final Integer age,
            final RequestSpecification restAssured
    ) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", id)
                .body(new MemberRequest(email, password, age))
                .when().put("/members/{id}")
                .then();
    }

    public static ValidatableResponse 회원_정보_수정_요청(
            final Long id, final String email, final String password, final Integer age
    ) {
        return 회원_정보_수정_요청(id, email, password, age, RestAssured.given());
    }

    public static void 회원_정보_수정_성공(
            final Long id, final String email, final String password, final Integer age
    ) {
        final var response = 회원_정보_수정_요청(id, email, password, age, RestAssured.given());
        statusCodeShouldBe(response, HttpStatus.OK);
    }

    public static ValidatableResponse 회원_삭제_요청(final Long id, final RequestSpecification restAssured) {
        return restAssured
                .pathParams("id", id)
                .when().delete("members/{id}")
                .then();
    }

    public static ValidatableResponse 회원_삭제_요청(final Long id) {
        return 회원_삭제_요청(id, RestAssured.given());
    }

    public static void 회원_삭제_성공(final Long id) {
        final var response = 회원_삭제_요청(id);
        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(final ExtractableResponse<Response> response) {
        final String uri = response.header("Location");
        return RestAssured.given()
                .when().delete(uri)
                .then().extract();
    }

    public static void 회원_정보_조회됨(final MemberResponse response, final String email, final int age) {
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getEmail()).isEqualTo(email),
                () -> assertThat(response.getAge()).isEqualTo(age)
        );
    }
}
