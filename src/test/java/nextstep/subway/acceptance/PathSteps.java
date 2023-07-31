package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.service.StationPathSearchRequestType;
import nextstep.subway.service.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.StationSteps.getStations;

public class PathSteps {
    private PathSteps() {
    }

    public static void 지하철_경로_조회(RequestSpecification specification, RestDocumentationFilter filter, Long sourceId, Long targetId) {
        RestAssured
                .given(specification).log().all()
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .when().get("/paths")
                .then().log().all().extract();
    }

    @Deprecated
    public static JsonPath searchStationPathOld(String startStation, String destinationStation, HttpStatus status) {
        final Map<String, Long> stationIdByName = getStations().getList("$", StationResponse.class)
                .stream()
                .collect(Collectors.toMap(StationResponse::getName, StationResponse::getId));

        return RestAssured.given().log().all()
                .queryParam("source", stationIdByName.get(startStation))
                .queryParam("target", stationIdByName.get(destinationStation))
                .get("/paths")
                .then().log().all()
                .statusCode(status.value())
                .extract()
                .jsonPath();
    }

    public static JsonPath searchStationPath(String startStation, String destinationStation, StationPathSearchRequestType type, HttpStatus status) {
        final Map<String, Long> stationIdByName = getStations().getList("$", StationResponse.class)
                .stream()
                .collect(Collectors.toMap(StationResponse::getName, StationResponse::getId));

        return RestAssured.given().log().all()
                .queryParam("source", stationIdByName.get(startStation))
                .queryParam("target", stationIdByName.get(destinationStation))
                .queryParam("type", type)
                .get("/paths")
                .then().log().all()
                .statusCode(status.value())
                .extract()
                .jsonPath();
    }
}
