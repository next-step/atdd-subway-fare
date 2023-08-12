package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.service.path.StationPathSearchRequestType;
import nextstep.subway.service.dto.StationPathResponse;
import nextstep.subway.service.dto.StationResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.StationSteps.getStations;

public class PathSteps {
    private PathSteps() {
    }

    public static void 지하철_경로_조회(RequestSpecification specification, RestDocumentationFilter filter, Long sourceId, Long targetId, StationPathSearchRequestType type) {
        RestAssured
                .given(specification).log().all()
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_경로_조회(String startStation, String destinationStation, StationPathSearchRequestType type, HttpStatus status) {
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
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_경로_조회(String userToken, String startStation, String destinationStation, StationPathSearchRequestType type, HttpStatus status) {
        final Map<String, Long> stationIdByName = getStations().getList("$", StationResponse.class)
                .stream()
                .collect(Collectors.toMap(StationResponse::getName, StationResponse::getId));

        return RestAssured.given().log().all()
                .auth().oauth2(userToken)
                .queryParam("source", stationIdByName.get(startStation))
                .queryParam("target", stationIdByName.get(destinationStation))
                .queryParam("type", type)
                .get("/paths")
                .then().log().all()
                .statusCode(status.value())
                .extract();
    }

    public static void 지하철_경로_조회됨(ExtractableResponse<Response> response, BigDecimal expectedDistance, BigDecimal expectedFee, Long expectedDuration, List<String> expectedStationNames) {
        var pathResponse = response.as(StationPathResponse.class);

        Assertions.assertEquals(0, expectedDistance.compareTo(pathResponse.getDistance()));
        Assertions.assertEquals(expectedDuration, pathResponse.getDuration());
        Assertions.assertEquals(0, expectedFee.compareTo(pathResponse.getFee()));
        Assertions.assertArrayEquals(expectedStationNames.toArray(), pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .toArray());
    }

}
