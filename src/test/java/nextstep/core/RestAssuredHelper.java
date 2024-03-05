package nextstep.core;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class RestAssuredHelper {
    private RestAssuredHelper() {
    }

    public static ExtractableResponse<Response> get(final String path) {
        return RestAssured
                .given()
                .when().get(path)
                .then().extract();
    }

    public static ExtractableResponse<Response> getWithAuth(final String path, final String accessToken) {
        return RestAssured
                .given()
                .auth().oauth2(accessToken)
                .when().get(path)
                .then().extract();
    }

    public static ExtractableResponse<Response> get(final String path, final Map<String, ?> parametersMap) {
        return RestAssured
                .given()
                .given().queryParams(parametersMap)
                .when().get(path)
                .then().extract();
    }

    public static ExtractableResponse<Response> getWithAuth(final String path, final String accessToken, final Map<String, ?> parametersMap) {
        return RestAssured
                .given()
                .auth().oauth2(accessToken)
                .queryParams(parametersMap)
                .when().get(path)
                .then().extract();
    }

    public static ExtractableResponse<Response> getById(final String path, final Long id) {
        return RestAssured
                .given().pathParam("id", id)
                .when().get(path + "/{id}")
                .then().extract();
    }

    public static ExtractableResponse<Response> post(final String path, final Object body) {
        return RestAssured
                .given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().extract();
    }

    public static ExtractableResponse<Response> postWithAuth(final String path, final String accessToken, final Object body) {
        return RestAssured
                .given()
                .auth().oauth2(accessToken)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().extract();
    }

    public static ExtractableResponse<Response> post(final String path, final Long pathVariable, final Object body) {
        return RestAssured
                .given().pathParam("id", pathVariable)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().extract();
    }

    public static ExtractableResponse<Response> put(final String path, final Long id, final Object body) {
        return RestAssured
                .given().pathParam("id", id)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path + "/{id}")
                .then().extract();
    }

    public static ExtractableResponse<Response> deleteById(final String path, final Long id) {
        return RestAssured
                .given().pathParam("id", id)
                .when().delete(path + "/{id}")
                .then().extract();
    }

    public static ExtractableResponse<Response> deleteByIdWithParam(final String path, final Long id, final Map<String, ?> parametersMap) {
        return RestAssured
                .given().pathParam("id", id)
                .queryParams(parametersMap)
                .when().delete(path)
                .then().extract();
    }

    public static ExtractableResponse<Response> deleteByIdWithAuth(final String path, final String accessToken, final Long id) {
        return RestAssured
                .given().pathParam("id", id)
                .auth().oauth2(accessToken)
                .when().delete(path + "/{id}")
                .then().extract();
    }

    public static Long getIdFromBody(final ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    public static Long getIdFromHeader(final ExtractableResponse<Response> response) {
        final String locationHeader = response.header("location");
        final String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
        return Long.parseLong(id);
    }

    public static <T> T findObjectFrom(final ExtractableResponse<Response> response, final Long id, final Class<T> type) {
        return response.jsonPath().getObject(String.format("find {it.id==%d}", id), type);
    }

}
