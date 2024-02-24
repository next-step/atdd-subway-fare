package nextstep.cucumber.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.List;

public class PathSteps {
    public static ExtractableResponse<Response> 경로_조회_요청(Long sourceId, Long targetId, String type) {
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

    public static List<String> parseStationNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("stations.name", String.class);
    }

    public static int parseDistance(ExtractableResponse<Response> response) {
        return response.jsonPath().getInt("distance");
    }

    public static int parseDuration(ExtractableResponse<Response> response) {
        return response.jsonPath().getInt("duration");
    }
}