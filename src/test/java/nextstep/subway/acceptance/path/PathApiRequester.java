package nextstep.subway.acceptance.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.PathType;

import static io.restassured.RestAssured.given;

public class PathApiRequester {

    public static ExtractableResponse<Response> getDistanceShortestPath(Long 출발역id, Long 도착역id) {
        return given().log().all()
                .queryParam("source", 출발역id)
                .queryParam("target", 도착역id)
                .queryParam("type", PathType.DISTANCE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getDurationShortestPath(Long 출발역id, Long 도착역id) {
        return given().log().all()
                .queryParam("source", 출발역id)
                .queryParam("target", 도착역id)
                .queryParam("type", PathType.DURATION)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
