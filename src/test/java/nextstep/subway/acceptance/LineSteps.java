package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps extends AcceptanceTestSteps {

    public static Long 지하철_노선_생성_요청(String token, String name, String color, Long upStation,
                                    Long downStation, int distance, int duration) {
        return 지하철_노선_생성_요청(token, name, color, upStation, downStation, distance, duration, 0);
    }

    public static Long 지하철_노선_생성_요청(String token, String name, String color, Long upStation,
                                    Long downStation, int distance, int duration, int fare) {
        Map<String, String> lineCreateParams = lineCreateParams(name, color, upStation, downStation, distance, duration, fare);
        return LineSteps.지하철_노선_생성_요청(token, lineCreateParams).jsonPath().getLong("id");
    }

    private static Map<String, String> lineCreateParams(String name, String color, Long upStation, Long downStation,
                                                        int distance, int duration, int fare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("fare", fare + "");

        return lineCreateParams;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String token, String name, String color) {
        return given(token)
                .body(Map.of("name", name, "color", color))
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

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String token, Map<String, String> params) {
        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String token, String location) {
        Map<String, String> params = new HashMap<>();
        params.put("color", "red");
        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location)
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String token, String location) {
        ExtractableResponse<Response> response = given(token)
                .when().delete(location)
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(String token, Long lineId, Map<String, String> params) {
        return given(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(String token, Long lineId, Long upStationId, Long downStationId, int distance, int duration) {
        return given(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createSectionCreateParams(upStationId, downStationId, distance, duration))
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    private static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(String token, Long lineId, Long stationId) {
        return given(token)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().extract();
    }
}
