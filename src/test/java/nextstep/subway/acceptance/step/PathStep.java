package nextstep.subway.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class PathStep {
    private PathStep() {
    }

    public static ExtractableResponse<Response> 최단거리_조회_요청(int sourceStationId, int targetStationId, RequestSpecification requestSpecification) {
        Map<String, Integer> params = Map.of(
                "source", sourceStationId,
                "target", targetStationId
        );

        return requestSpecification
                .queryParams(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
