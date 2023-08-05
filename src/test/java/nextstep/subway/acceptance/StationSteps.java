package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import nextstep.subway.service.dto.StationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StationSteps {
    private StationSteps() {
    }

    public static Long createStation(String name) {
        var request = new StationRequest(name);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/stations")
                .then().log().all()
                .extract()
                .jsonPath().getLong("id");
    }

    public static List<Long> createStations(List<String> names) {
        return names.stream()
                .map(StationSteps::createStation)
                .collect(Collectors.toList());
    }

    public static JsonPath getStations() {
        return RestAssured.given().log().all()
                .get("/stations")
                .then().log().all()
                .extract()
                .jsonPath();
    }

    public static void deleteStation(Long stationId) {
        RestAssured.given().log().all()
                .when().delete("stations/" + stationId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static Map<String, Long> createStationsAndGetStationMap(List<String> names) {
        return names.stream()
                .collect(Collectors.toMap(Function.identity(), StationSteps::createStation));
    }
}
