package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_소요시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .param("source", source)
                .param("target", target)
                .param("type", "DURATION")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_소요시간_경로_조회를_요청(String token, Long source, Long target) {
        return given(token).log().all()
                           .param("source", source)
                           .param("target", target)
                           .param("type", "DURATION")
                           .accept(MediaType.APPLICATION_JSON_VALUE)
                           .when().get("/paths")
                           .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .param("source", source)
                .param("target", target)
                .param("type", "DISTANCE")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all().extract();
    }


    static RequestSpecification given(String token) {
        return RestAssured.given().log().all()
                          .auth().oauth2(token);
    }
}
