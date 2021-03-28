package nextstep.subway.line.acceptance.linesection;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class LineSectionRequestSteps {

    public static LineRequest 노선_요청(String name, String color, Long upStationId, Long downStationId, int distance) {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    public static ExtractableResponse<Response> 지하철_노선에_구간_등록_요청(Long lineId, Long upStationId, Long downStationId, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when()
                .pathParam("id", lineId)
                .post("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    // TODO : 보완, Dutation
//    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance, int duration) {
//        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance, duration);
//
//        return RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(sectionRequest)
//                .when().post("/lines/{lineId}/sections", line.getId())
//                .then().log().all().extract();
//    }

    public static ExtractableResponse<Response> 지하철_노선에_등록된_구간_제거_요청(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .param("stationId", stationId)
                .when()
                .pathParam("id", lineId)
                .delete("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }
}
