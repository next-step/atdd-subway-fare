package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.error.SubwayError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.common.error.MemberError.NOT_VALID_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(final String accessToken, final Long sourceId, final Long targetId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(final String accessToken, final Long favoriteId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String accessToken, final Long favoriteId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    public static void 권한없어서_실패됨(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value()),
                () -> assertThat(response.jsonPath().getString("message")).contains(NOT_VALID_TOKEN.getMessage())
        );
    }

    public static void 즐겨찾기_생성됨(final ExtractableResponse<Response> response, final Long sourceId, final Long targetId) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(response.jsonPath().getLong("source.id")).isEqualTo(sourceId),
                () -> assertThat(response.jsonPath().getLong("target.id")).isEqualTo(targetId)
        );
    }

    public static void 즐겨찾기_조회됨(final ExtractableResponse<Response> response, final Long sourceId, final Long targetId) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.jsonPath().getLong("source.id")).isEqualTo(sourceId),
                () -> assertThat(response.jsonPath().getLong("target.id")).isEqualTo(targetId)
        );
    }

    public static void 즐겨찾기_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    public static void 요청값_누락으로_실패됨(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).contains("필수값이 존재하지 않습니다.")
        );
    }

    public static void 즐겨찾기_조회_없음(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value()),
                () -> assertThat(response.jsonPath().getString("message")).contains(SubwayError.NOT_FOUND.getMessage())
        );
    }

    public static void 즐겨찾기_삭제대상_없음(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value())
        );
    }

    public static void 역이_등록되어_있지_않아서_실패됨(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value())
        );
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
