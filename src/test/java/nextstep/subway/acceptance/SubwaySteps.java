package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.path.PathType;
import nextstep.subway.line.section.dto.SectionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

public class SubwaySteps {
    public static ExtractableResponse<Response> 지하철_역_생성(String stationName) {
        return RestAssured.given()
                .body(Map.of("name", stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(LineRequest lineRequest) {
        return RestAssured
                .given()
                .when()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_구간_생성(Long lineId, SectionRequest sectionRequest) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().log().all()
                .post("/lines/" + lineId + "/sections")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_최단경로_조회(Long sourceStation, Long targetStation, PathType type) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceStation)
                .param("target", targetStation)
                .param("type", type.name())
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        return response;
    }

    public static void 지하철_최단경로_조회_BAD_REQUEST(Long sourceStation, Long targetStation, PathType type) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceStation)
                .param("target", targetStation)
                .param("type", type.name())
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
