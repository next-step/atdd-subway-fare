package nextstep.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.line.application.request.LineCreateRequest;
import nextstep.line.application.request.LineModifyRequest;
import nextstep.line.application.request.SectionAddRequest;
import nextstep.line.domain.path.ShortPathType;
import org.springframework.http.MediaType;

import static nextstep.station.acceptance.StationRequester.createStationThenReturnId;

public class LineRequester {

    public static ExtractableResponse<Response> findLine(Long id) {
        return findLineRequest(id);
    }

    public static ExtractableResponse<Response> findLines() {
        return findLinesRequest();
    }

    public static Long createLineThenReturnId(String name, String color, String upStationName, String downStationName, int distance, int duration) {
        return createLineRequest(name, color, createStationThenReturnId(upStationName), createStationThenReturnId(downStationName), distance, duration)
                .jsonPath().getObject("id", Long.class);
    }

    public static Long createLineThenReturnId(String name, String color, Long upStationId, Long downStationId, int distance, int duration) {
        return createLineRequest(name, color, upStationId, downStationId, distance, duration)
                .jsonPath().getObject("id", Long.class);
    }

    public static ExtractableResponse<Response> modifyLine(Long id, String name, String color) {
        return modifyLineRequest(id, name, color);
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {
        return deleteLineRequest(id);
    }

    public static ExtractableResponse<Response> addSection(Long id, Long upStationId, Long downStationId, int distance, int duration) {
        return addSectionRequest(id, upStationId, downStationId, distance, duration);
    }

    public static ExtractableResponse<Response> deleteSection(Long id, Long stationId) {
        return deleteSectionRequest(id, stationId);
    }

    public static ExtractableResponse<Response> findShortPath(ShortPathType type, Long startStationId, Long endStationId) {
        return findShortPathRequest(type, startStationId, endStationId);
    }

    public static ExtractableResponse<Response> findShortPathForDucument(RequestSpecification requestSpecification,
                                                                         Filter filter,
                                                                         ShortPathType type,
                                                                         Long startStationId,
                                                                         Long endStationId) {
        return findShortPathRequestForDocument(requestSpecification, filter, type, startStationId, endStationId);
    }

    private static ExtractableResponse<Response> findLineRequest(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> findLinesRequest() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> createLineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int duration) {
        return RestAssured.given().log().all()
                .body(new LineCreateRequest(name, color, upStationId, downStationId, distance, duration))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> modifyLineRequest(Long id, String name, String color) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .body(new LineModifyRequest(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> deleteLineRequest(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> addSectionRequest(Long id, Long upStationId, Long downStationId, int distance, int duration) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .body(new SectionAddRequest(upStationId, downStationId, distance, duration))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> deleteSectionRequest(Long id, Long stationId) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .param("stationId", stationId)
                .when().delete("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> findShortPathRequest(ShortPathType type, Long startStationId, Long endStationId) {
        return RestAssured.given().log().all()
                .param("startStationId", startStationId)
                .param("endStationId", endStationId)
                .param("type", type)
                .when().get("/lines/paths")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> findShortPathRequestForDocument(RequestSpecification requestSpecification,
                                                                                 Filter filter,
                                                                                 ShortPathType type,
                                                                                 Long startStationId,
                                                                                 Long endStationId) {
        return RestAssured.given(requestSpecification).log().all()
                .filter(filter)
                .param("startStationId", startStationId)
                .param("endStationId", endStationId)
                .param("type", type)
                .when().get("/lines/paths")
                .then().log().all()
                .extract();
    }
}
