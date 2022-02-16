package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(RequestSpecification given, Map<String, String> params) {
        return given.log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines")
                    .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return 지하철_노선_생성_요청(RestAssured.given().log().all(), params);
    }

    public static Long 지하철_노선_생성_요청_하고_ID_반환(Map<String, String> params) {
        return 지하철_노선_생성_요청(RestAssured.given().log().all(), params).jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse) {
        return RestAssured
                .given().log().all()
                .when().get(createResponse.header("location"))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().extract();
    }

    public static Map<String, String> createLineCreateParams(String name, String color, long upStationId,
                                                             long downStationId, int distance, int duration,
                                                             int additionalFare, LocalTime startTime, LocalTime endTime, LocalTime intervalTime) {
        Map<String, String> lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("additionalFare", additionalFare + "");
        lineCreateParams.put("startTime", startTime + "");
        lineCreateParams.put("endTime", endTime + "");
        lineCreateParams.put("intervalTime", intervalTime + "");
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        return lineCreateParams;
    }

    public static Map<String, String> createLineCreateParams(String name, String color, long upStationId, long downStationId,
                                                             int distance, int duration, int additionalFare) {
        return createLineCreateParams(
            name, color, upStationId, downStationId, distance, duration, additionalFare,
            LocalTime.of(5, 0), LocalTime.of(23, 0), LocalTime.of(0, 10)
        );
    }

    public static Map<String, String> createLineCreateParams(String name, String color, int additionalFare) {
        long UP_STATION_ID = 0;
        long DOWN_STATION_ID = 0;
        int DISTANCE = 0;
        int DURATION = 0;
        return createLineCreateParams(
            name, color, UP_STATION_ID, DOWN_STATION_ID, DISTANCE, DURATION, additionalFare,
            LocalTime.of(5, 0), LocalTime.of(23, 0), LocalTime.of(0, 10)
        );
    }

    public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
