package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        return 지하철_노선_생성_요청(RestAssured.given().log().all(), name, color);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(RequestSpecification requestSpecification,
                                                             String name,
                                                             String color) {
        Map<String, String> params = getLineCreateParams(name, color);

        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(RequestSpecification requestSpecification,
                                                             Map<String, String> params) {

        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String accessToken, Map<String, String> params) {
        return 지하철_노선_생성_요청(RestAssured.given().log().all().auth().oauth2(accessToken), params);
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
        return 지하철_노선_생성_요청(RestAssured.given().log().all(), params);
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

    public static Map<String, String> getLineCreateParams(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    public static Map<String, String> getLineCreateParams(String name,
                                                          String color,
                                                          Long upStationId,
                                                          Long downStationId,
                                                          int distance,
                                                          int duration) {
        Map<String, String> params = getLineCreateParams(name, color);
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
