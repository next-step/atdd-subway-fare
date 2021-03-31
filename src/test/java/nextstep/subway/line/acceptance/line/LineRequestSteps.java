package nextstep.subway.line.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.line.dto.LineRequest;
import org.springframework.http.MediaType;

public class LineRequestSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(RequestSpecification given, LineRequest lineRequest) {
        return given
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(RequestSpecification given) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(RequestSpecification given, Long lineId) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .pathParam("lineId", lineId)
                .get("/lines/{lineId}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(RequestSpecification given, Long lineId, String name, String color) {
        return given
                .body(new LineRequest(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .pathParam("lineId", lineId)
                .put("/lines/{lineId}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(RequestSpecification given, Long lineId) {
        return given
                .when()
                .pathParam("lineId", lineId)
                .delete("/lines/{lineId}")
                .then().log().all()
                .extract();
    }
}
