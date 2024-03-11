package nextstep.subway.acceptance.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PathSteps {
    public static ExtractableResponse<Response> 최단경로_조회요청(Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}&type=DISTANCE", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 최소시간경로_조회요청(Long sourceId, Long targetId) {
        return RestAssured.given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}&type=DURATION", sourceId, targetId)
                .then().log().all()
                .extract();
    }
}
