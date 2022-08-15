package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTestSteps;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps extends AcceptanceTestSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String token, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String token, String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("intervalTime", intervalTime);
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

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String token, ExtractableResponse<Response> response,
                                                             String color, LocalTime startTime, LocalTime endTime, int intervalTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("color", color);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("intervalTime", intervalTime);
        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(response.header("Location"))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String token, String location) {
        ExtractableResponse<Response> response = given(token)
                .when().delete(location)
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String token, ExtractableResponse<Response> response) {
        return given(token)
                .when().delete(response.header("Location"))
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

    public static void 지하철_노선_검증(ExtractableResponse<Response> response, String name, String color, String startTime, String endTime, int intervalTime) {
        assertThat(response.jsonPath().getString("name")).isEqualTo(name);
        assertThat(response.jsonPath().getString("color")).isEqualTo(color);
        assertThat(response.jsonPath().getString("startTime")).isEqualTo(startTime);
        assertThat(response.jsonPath().getString("endTime")).isEqualTo(endTime);
        assertThat(response.jsonPath().getInt("intervalTime")).isEqualTo(intervalTime);
    }
}
