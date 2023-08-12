package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import nextstep.subway.service.dto.StationLineCreateRequest;
import nextstep.subway.service.dto.StationLineUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Map;

public class StationLineSteps {
    private StationLineSteps() {
    }

    public static Long createStationLine(String name, String color, String upStationName, String downStationName, BigDecimal distance, Long duration, BigDecimal additionalFee, Map<String, Long> stationIdByName) {
        return createStationLine(name, color, stationIdByName.get(upStationName), stationIdByName.get(downStationName), distance, duration, additionalFee);
    }

    public static Long createStationLine(String name, String color, Long upStationId, Long downStationId, BigDecimal distance, Long duration, BigDecimal additionalFee) {
        var request = new StationLineCreateRequest(name, color, upStationId, downStationId, distance, duration, additionalFee);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getLong("id");
    }

    public static void updateStationLine(Long lineId, String name, String color) {
        var request = new StationLineUpdateRequest(name, color);

        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    public static void deleteStationLine(Long lineId) {
        RestAssured.given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    public static JsonPath getStationLine(Long stationLineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + stationLineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath();
    }

    public static JsonPath getStationLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath();
    }
}
