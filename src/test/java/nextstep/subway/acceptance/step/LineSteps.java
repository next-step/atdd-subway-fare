package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    public static final String 신분당선_이름 = "신분당선";
    public static final String 신분당선_색 = "red";
    public static final String 이호선_이름 = "2호선";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color,
                                                             long upStationId, long downStationId,
                                                             int distance, int duration) {
        Map<String, String> params = 노선_생성_Param_생성(name, color, upStationId, downStationId, distance, duration);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color,
                                                             long upStationId, long downStationId) {
        return 지하철_노선_생성_요청(name, color, upStationId, downStationId, 100, 10);
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

    public static Map<String, String> 노선_생성_Param_생성(String name, String color,
                                                     long upStationId, long downStationId,
                                                     int distance, int duration) {
        Map<String, String> params = new HashMap();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));
        params.put("duration", String.valueOf(duration));

        return params;
    }

    public static void 노선_생성_응답상태_검증(ExtractableResponse<Response> response) {
        노선_응답_상태코드_검증(response.statusCode(), HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 노선_생성_실패_응답상태_검증(ExtractableResponse<Response> response) {
        노선_응답_상태코드_검증(response.statusCode(), HttpStatus.BAD_REQUEST);
    }

    public static void 노선_조회_응답상태_검증(ExtractableResponse<Response> response) {
        노선_응답_상태코드_검증(response.statusCode(), HttpStatus.OK);
    }

    public static void 노선_삭제_응답상태_검증(ExtractableResponse<Response> response) {
        노선_응답_상태코드_검증(response.statusCode(), HttpStatus.NO_CONTENT);
    }

    public static void 노선_응답_상태코드_검증(int statusCode, HttpStatus httpStatus) {
        assertThat(statusCode).isEqualTo(httpStatus.value());
    }

}
