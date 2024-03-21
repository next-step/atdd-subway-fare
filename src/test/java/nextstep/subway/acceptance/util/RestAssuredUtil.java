package nextstep.subway.acceptance.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

public class RestAssuredUtil {
    public static ExtractableResponse<Response> 생성_요청(Object params, String path) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 조회_요청(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 경로_조회_요청(String path, Long source, Long target, PathType type, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .param("source", source)
                .param("target", target)
                .param("type", type)
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 수정_요청(Object param, String path) {
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 삭제_요청(String path) {
        return RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all()
                .extract();
    }
}
