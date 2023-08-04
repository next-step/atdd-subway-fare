package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.Map;

public class PathStep {
    private PathStep() {
    }

    public static ExtractableResponse<Response> 출발_역에서_도착_역까지의_최단거리_조회(int sourceStationId, int targetStationId) {
        RequestSpecification requestSpecification = RestAssured.given().log().all();

        return 최단거리_조회_요청(sourceStationId, targetStationId, requestSpecification);
    }

    public static ExtractableResponse<Response> 출발_역에서_도착_역까지의_최단거리_조회_문서화(RequestSpecification spec, RestDocumentationFilter document) {
        RequestSpecification requestSpecification = RestAssured.given(spec).log().all()
                .filter(document);

        return 최단거리_조회_요청(1, 2, requestSpecification);
    }

    private static ExtractableResponse<Response> 최단거리_조회_요청(int sourceStationId, int targetStationId, RequestSpecification requestSpecification) {
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
