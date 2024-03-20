package nextstep.subway.acceptance.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 로그인하지않은사용자_경로_조회요청(Long sourceId, Long targetId, PathType type) {
        return RestAssured.given().log().all()
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", type)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 경로_조회요청(Long sourceId, Long targetId, PathType type, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
