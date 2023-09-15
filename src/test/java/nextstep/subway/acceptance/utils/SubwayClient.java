package nextstep.subway.acceptance.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;
import nextstep.subway.dto.LineRequest;
import nextstep.subway.dto.SectionRequest;
import nextstep.subway.dto.StationRequest;

public class SubwayClient {

    public static ExtractableResponse<Response> 지하철역_생성_요청(StationRequest param) {
        return RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_생성_요청(LineRequest param) {
        return RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_조회_요청(Long lineId) {
        return RestAssured.given().log().all()
            .when().get("/lines/" + lineId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_조회_요청() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_수정_요청(Long lineId, LineRequest param) {
        return RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/" + lineId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(Long lineId) {
        return RestAssured.given().log().all()
            .when().delete("/lines/" + lineId)
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 구간_생성_요청(Long lineId, SectionRequest param) {
        return RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
            .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 경로_조회_요청(Long source, Long target, PathType type) {
        return 경로_조회_요청(new RequestSpecBuilder().build(), source, target, type);
    }

    public static ExtractableResponse<Response> 경로_조회_요청(RequestSpecification spec, Long source, Long target, PathType type) {
        return RestAssured.given(spec).log().all()
            .when().get("/paths?source=" + source + "&target=" + target + "&type=" + type)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 경로_조회_요청() {
        return RestAssured.given().log().all()
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

}
