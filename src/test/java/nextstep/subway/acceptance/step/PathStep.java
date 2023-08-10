package nextstep.subway.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import java.util.Objects;

public class PathStep {
    private PathStep() {
    }

    public static ExtractableResponse<Response> 경로_조회_요청(int sourceStationId, int targetStationId, String type, RequestSpecification requestSpecification) {
        Map<String, Object> params = Map.of(
                "source", sourceStationId,
                "target", targetStationId,
                "type", type
        );

        return requestSpecification
                .queryParams(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
