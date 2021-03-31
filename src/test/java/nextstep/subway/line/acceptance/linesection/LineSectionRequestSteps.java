package nextstep.subway.line.acceptance.linesection;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import org.springframework.http.MediaType;

public class LineSectionRequestSteps {

    public static LineRequest 노선_요청(String name, String color, Long upStationId, Long downStationId, int distance, int duration) {
        return new LineRequest(name, color, upStationId, downStationId, distance, duration);
    }

    public static ExtractableResponse<Response> 지하철_노선에_구간_등록_요청(RequestSpecification given, Long lineId, Long upStationId, Long downStationId, int distance, int duration) {
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance, duration);

        return given
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when()
                .pathParam("lineId", lineId)
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_등록된_구간_제거_요청(RequestSpecification given, Long lineId, Long stationId) {
        return given
                .param("stationId", stationId)
                .when()
                .pathParam("lineId", lineId)
                .delete("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }
}
