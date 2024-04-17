package nextstep.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> getPath(Long source, Long target, String type, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
