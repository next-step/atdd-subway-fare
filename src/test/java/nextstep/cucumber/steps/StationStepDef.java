package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStepDef implements En {
    ExtractableResponse<Response> response;
    @Autowired
    private AcceptanceContext acceptanceContext;

    public StationStepDef() {
        When("지하철역을 생성하면", () -> {
            Map<String, String> params = new HashMap<>();
            params.put("name", "강남역");
            response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/stations")
                    .then().log().all()
                    .extract();
        });

        Then("지하철역이 생성된다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Given("지하철 역들을 생성한다", (DataTable dataTable) -> {
            List<String> stationNames = dataTable.asList();
            for (String name : stationNames) {
                ExtractableResponse<Response> stationResponse = RestAssured.given()
                        .body(Map.of("name", name))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();
                putStation(name, stationResponse);
            }
        });

        Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다", () -> {
            List<String> stationNames =
                    RestAssured.given().log().all()
                            .when().get("/stations")
                            .then().log().all()
                            .extract().jsonPath().getList("name", String.class);
            assertThat(stationNames).containsAnyOf("강남역");
        });

        Given("지하철역이 등록되어 있다", () -> {
            assertThat(findStation("교대역")).isNotNull();
            assertThat(findStation("강남역")).isNotNull();
            assertThat(findStation("양재역")).isNotNull();
            assertThat(findStation("남부터미널역")).isNotNull();
        });
    }

    private void putStation(String name, ExtractableResponse<Response> response) {
        StationResponse responseObj = response.as(StationResponse.class);
        acceptanceContext.putStation(name, new Station(responseObj.getId(), responseObj.getName()));
    }

    private Station findStation(String name) {
        return acceptanceContext.findStation(name);
    }
}
