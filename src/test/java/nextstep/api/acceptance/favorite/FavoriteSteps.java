package nextstep.api.acceptance.favorite;

import static nextstep.api.acceptance.AcceptanceHelper.asResponses;
import static nextstep.api.acceptance.AcceptanceHelper.statusCodeShouldBe;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.favorite.application.dto.FavoriteRequest;
import nextstep.api.favorite.application.dto.FavoriteResponse;

public class FavoriteSteps {

    public static final String BASE_URL = "/favorites";

    public static ValidatableResponse 즐겨찾기_생성_요청(
            final String token, final Long source, final Long target, final RequestSpecification restAssured
    ) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(new FavoriteRequest(source, target))
                .when().post(BASE_URL)
                .then();
    }

    public static ValidatableResponse 즐겨찾기_생성_요청(final String token, final Long source, final Long target) {
        return 즐겨찾기_생성_요청(token, source, target, RestAssured.given());
    }

    public static void 즐겨찾기_생성_성공(final String token, final Long source, final Long target) {
        final var response = 즐겨찾기_생성_요청(token, source, target);
        statusCodeShouldBe(response, HttpStatus.CREATED);
    }

    public static ValidatableResponse 즐겨찾기_제거_요청(
            final String token, final Long id, final RequestSpecification restAssured
    ) {
        return restAssured
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .pathParams("id", id)
                .when().delete(BASE_URL + "/{id}")
                .then();
    }

    public static ValidatableResponse 즐겨찾기_제거_요청(final String token, final Long id) {
        return 즐겨찾기_제거_요청(token, id, RestAssured.given());
    }

    public static void 즐겨찾기_제거_성공(final String token, final Long id) {
        final var response = 즐겨찾기_제거_요청(token, id);
        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }

    public static ValidatableResponse 즐겨찾기_전체조회_요청(
            final String token, final RequestSpecification restAssured
    ) {
        return restAssured
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when().get(BASE_URL)
                .then();
    }

    public static ValidatableResponse 즐겨찾기_전체조회_요청(final String token) {
        return 즐겨찾기_전체조회_요청(token, RestAssured.given());
    }

    public static List<FavoriteResponse> 즐겨찾기_전체조회_성공(final String token) {
        final var response = 즐겨찾기_전체조회_요청(token);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponses(response, FavoriteResponse.class);
    }
}
