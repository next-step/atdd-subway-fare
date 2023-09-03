package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineSteps {
    private LineSteps() {
    }

    public static ExtractableResponse<Response> 지하철_노선을_생성한다(Long upStationId, Long downStationId, String lineName, int distance, int duration) {
        return 지하철_노선을_생성한다(lineName, "bg-red-600", upStationId, downStationId, distance, duration, 0);
    }


    public static ExtractableResponse<Response> 지하철_노선을_생성한다(String lineName, String color, Long upStationId, Long downStationId, int distance, int duration, int additionalFee) {
        Map<String, Object> params = Map.of(
                "name", lineName,
                "color", color,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance,
                "duration", duration,
                "additionalFee", additionalFee
        );

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_조회한다(long lineId) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_수정한다(long lineId, String lineName, String color) {
        Map<String, String> params = Map.of(
                "name", lineName,
                "color", color
        );

        return RestAssured.given().log().all()
                .body(params)
                .pathParam("id", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_삭제한다(long lineId) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
    }
}
