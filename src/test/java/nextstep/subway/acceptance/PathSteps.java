package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 경로_조회_요청(Long source, Long target, String type) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 경로_조회_요청(String accessToken, Long source, Long target, String type) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 경로_조회_요청(Long source, Long target, String type, RequestSpecification requestSpecification) {
        return requestSpecification
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
