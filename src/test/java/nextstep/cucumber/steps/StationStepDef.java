package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.LineRequest;
import nextstep.subway.line.section.SectionRequest;
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

    private Long getIdFromStore(String name) {
        return ((ExtractableResponse<Response>) acceptanceContext.store.get(name)).jsonPath().getLong("id");
    }

    public StationStepDef() {
        DataTableType((Map<String, String> entry) -> new LineRequest(
                entry.get("이름"),
                entry.get("색상"),
                getIdFromStore(entry.get("출발역")),
                getIdFromStore(entry.get("도착역")),
                Long.parseLong(entry.get("거리"))
        ));

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

        Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다", () -> {
            List<String> stationNames =
                    RestAssured.given().log().all()
                            .when().get("/stations")
                            .then().log().all()
                            .extract().jsonPath().getList("name", String.class);
            assertThat(stationNames).containsAnyOf("강남역");
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
                acceptanceContext.store.put(name, stationResponse);
            }
        });

        And("지하철 노선들을 생성한다", (DataTable dataTable) -> {
            List<LineRequest> lineRequests = dataTable.asList(LineRequest.class);
            for (LineRequest request : lineRequests) {
                ExtractableResponse<Response> lineResponse = RestAssured
                        .given()
                        .when()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .post("/lines")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();
                acceptanceContext.store.put(request.getName(), lineResponse);
            }
        });

        And("{string}노선에 {string}에서 {string}까지 거리가 {long}인 지하철 구간을 추가한다", (String line, String upStation, String downStation, Long distance) -> {
            RestAssured
                    .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new SectionRequest(
                            getIdFromStore(upStation),
                            getIdFromStore(downStation),
                            distance)
                    )
                    .when().log().all()
                    .post("/lines/" + getIdFromStore(line) + "/sections")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        });

        When("{string}부터 {string}까지의 최단 경로를 조회하면", (String source, String target) -> {
            response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("source", getIdFromStore(source))
                    .param("target", getIdFromStore(target))
                    .when()
                    .get("/paths")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });

        Then("출발역과 도착역 사이의 최단 경로 정보를 응답한다", () -> {
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(getIdFromStore("교대역"), getIdFromStore("남부터미널역"), getIdFromStore("양재역"));
            assertThat(response.jsonPath().getDouble("distance")).isEqualTo(23);
        });
    }

}
