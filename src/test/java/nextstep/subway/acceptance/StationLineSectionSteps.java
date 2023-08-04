package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.service.dto.StationLineSectionCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StationLineSectionSteps {
    private StationLineSectionSteps() {
    }

    public static void createStationLineSection(Long lineId, String upStationName, String downStationName, BigDecimal distance, Long duration, Map<String, Long> stationIdByName) {
        createStationLineSection(lineId, stationIdByName.get(upStationName), stationIdByName.get(downStationName), distance, duration, HttpStatus.OK);
    }


    public static void createStationLineSection(Long lineId, Long upStationId, Long downStationId, BigDecimal distance, Long duration) {
        createStationLineSection(lineId, upStationId, downStationId, distance, duration, HttpStatus.OK);
    }

    public static void createStationLineSection(Long lineId, Long upStationId, Long downStationId, BigDecimal distance, Long duration, HttpStatus expectedStatus) {
        StationLineSectionCreateRequest request = new StationLineSectionCreateRequest();

        request.setUpStationId(upStationId);
        request.setDownStationId(downStationId);
        request.setDistance(distance);
        request.setDuration(duration);

        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .statusCode(expectedStatus.value())
                .extract();
    }

    public static void deleteStationLineSection(Long lineId, Long stationId, HttpStatus expectedStatus) {
        RestAssured.given().log().all()
                .param("stationId", stationId)
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .statusCode(expectedStatus.value())
                .extract();
    }
}
