package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineSteps {
    public static Long 추가요금_존재하는_지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraFare) {
        return 지하철_노선_생성_요청(createLineParams(name, color, upStation, downStation, distance, duration, extraFare)).jsonPath().getLong("id");
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        return 지하철_노선_생성_요청(createLineParams(name, color, upStation, downStation, distance, duration, 0)).jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        return 지하철_노선_생성_요청(createLineParams(name, color, 0L, 0L, 0, 0, 0));
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String accessToken, Map<String, String> params) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all().extract();
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

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");

        return 지하철_노선에_지하철_구간_생성_요청(lineId, params);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines/{lineId}/sections", lineId)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(String accessToken, Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
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

    private static Map<String, String> createLineParams(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraFare) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStation + "");
        params.put("downStationId", downStation + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        params.put("extraFare", extraFare + "");
        return params;
    }
}
