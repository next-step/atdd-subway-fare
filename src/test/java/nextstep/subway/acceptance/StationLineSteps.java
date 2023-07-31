package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StationLineSteps {
    private StationLineSteps() {
    }

    public static Long createStationLine(String name, String color, String upStationName, String downStationName, BigDecimal distance, Map<String, Long> stationIdByName) {
        return createStationLine(name, color, stationIdByName.get(upStationName), stationIdByName.get(downStationName), distance);
    }

    @Deprecated
    public static Long createStationLine(String name, String color, Long upStationId, Long downStationId, BigDecimal distance) {
        final Map<String, String> stationLineCreateRequest = new HashMap<>();

        stationLineCreateRequest.put("name", name);
        stationLineCreateRequest.put("color", color);
        stationLineCreateRequest.put("upStationId", String.valueOf(upStationId));
        stationLineCreateRequest.put("downStationId", String.valueOf(downStationId));
        stationLineCreateRequest.put("distance", distance.toString());

        return RestAssured.given().log().all()
                .body(stationLineCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getLong("id");
    }

    public static Long createStationLine(String name, String color, Long upStationId, Long downStationId, BigDecimal distance, Long duration) {
        final Map<String, String> stationLineCreateRequest = new HashMap<>();

        stationLineCreateRequest.put("name", name);
        stationLineCreateRequest.put("color", color);
        stationLineCreateRequest.put("upStationId", String.valueOf(upStationId));
        stationLineCreateRequest.put("downStationId", String.valueOf(downStationId));
        stationLineCreateRequest.put("distance", distance.toString());
        stationLineCreateRequest.put("duration", duration.toString());

        return RestAssured.given().log().all()
                .body(stationLineCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getLong("id");
    }

    public static void updateStationLine(Long lineId, String name, String color) {
        final Map<String, String> stationLineUpdateRequest = new HashMap<>();

        stationLineUpdateRequest.put("name", name);
        stationLineUpdateRequest.put("color", color);

        RestAssured.given().log().all()
                .body(stationLineUpdateRequest)
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
