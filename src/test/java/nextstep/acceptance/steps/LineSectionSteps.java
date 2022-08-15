package nextstep.acceptance.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSectionSteps extends AcceptanceTestSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String token, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return 지하철_노선_생성_요청(token, params);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String token, String name, String color, int extraFare) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("extraFare", extraFare + "");

        return 지하철_노선_생성_요청(token, params);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String token, Map<String, String> params) {
        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return given()
                .when().get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse) {
        return given()
                .when().get(createResponse.header("location"))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return given()
                .when().get("/lines/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String token, String location, Map<String, String> params) {
        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location)
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String token, String location) {
        return given(token)
                .when().delete(location)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(String token, Long lineId, Map<String, String> params) {
        return given(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(String token, Long lineId, Long stationId) {
        return given(token)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().extract();
    }

    public static Map<String, String> createLineCreateParams(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        return createLineCreateParams(name, color, 0, upStation, downStation, distance, duration);
    }

    public static Map<String, String> createLineCreateParams(String name,
                                                             String color,
                                                             int extraFare,
                                                             Long upStation,
                                                             Long downStation,
                                                             int distance,
                                                             int duration) {

        Map<String, String> params;
        params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("extraFare", extraFare + "");
        params.put("upStationId", upStation + "");
        params.put("downStationId", downStation + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

    public static Map<String, String> createLineUpdateParams(String name, String color, int extraFare) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("extraFare", extraFare + "");
        return params;
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
