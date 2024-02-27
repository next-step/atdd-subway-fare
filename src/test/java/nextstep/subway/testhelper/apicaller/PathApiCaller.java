package nextstep.subway.testhelper.apicaller;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.application.dto.PathRequest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class PathApiCaller {

    public static ExtractableResponse<Response> 경로_조회(PathRequest pathRequest) {
        return given().log().all()
                .queryParam("source", pathRequest.getSource())
                .queryParam("target", pathRequest.getTarget())
                .queryParam("type", pathRequest.getType())
                .when().get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 경로_조회(String token,
                                                      PathRequest pathRequest) {
        return given().log().all()
                .auth().oauth2(token)
                .queryParam("source", pathRequest.getSource())
                .queryParam("target", pathRequest.getTarget())
                .queryParam("type", pathRequest.getType())
                .when().get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
