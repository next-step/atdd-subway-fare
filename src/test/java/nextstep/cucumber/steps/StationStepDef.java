package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.path.PathType;
import nextstep.subway.line.section.domain.Section;
import nextstep.subway.line.section.dto.SectionRequest;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStepDef implements En {
    ExtractableResponse<Response> response;

    @Autowired
    private AcceptanceContext acceptanceContext;

    public StationStepDef() {
        DataTableType((Map<String, String> entry) -> new LineRequest(
                entry.get("이름"),
                entry.get("색상"),
                findStation(entry.get("출발역")).getId(),
                findStation(entry.get("도착역")).getId(),
                Long.parseLong(entry.get("거리")),
                Long.parseLong(entry.get("소요시간"))
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
                putStation(name, stationResponse);
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
                putLine(request, lineResponse);
            }
        });

        And("{string}노선에 {string}에서 {string}까지 거리가 {long}, 소요 시간이 {long}인 지하철 구간을 추가한다", (String line, String upStation, String downStation, Long distance, Long duration) -> {
            RestAssured
                    .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new SectionRequest(
                            findStation(upStation).getId(),
                            findStation(downStation).getId(),
                            distance,
                            duration)
                    )
                    .when().log().all()
                    .post("/lines/" + findLine(line).getId() + "/sections")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            addSection(line, upStation, downStation, distance, duration);
        });

        Given("지하철역이 등록되어 있다", () -> {
            assertThat(findStation("교대역")).isNotNull();
            assertThat(findStation("강남역")).isNotNull();
            assertThat(findStation("양재역")).isNotNull();
            assertThat(findStation("남부터미널역")).isNotNull();
        });

        Given("지하철 노선이 등록되어 있다", () -> {
            assertThat(findLine("이호선")).isNotNull();
            assertThat(findLine("신분당선")).isNotNull();
            assertThat(findLine("삼호선")).isNotNull();
        });

        Given("지하철 노선에 지하철역이 등록되어있다", () -> {
            assertThat(findLine("이호선").getSections().allStations()).hasSize(2);
            assertThat(findLine("신분당선").getSections().allStations()).hasSize(2);
            assertThat(findLine("삼호선").getSections().allStations()).hasSize(3);
        });

        When("{string}부터 {string}까지의 {string} 경로를 조회하면", (String source, String target, String type) -> {
            PathType pathType = type.equals("최단 거리") ? PathType.DISTANCE : PathType.DURATION;
            response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("source", findStation(source).getId())
                    .param("target", findStation(target).getId())
                    .param("type", pathType)
                    .when()
                    .get("/paths")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();
        });

        Then("{string} 경로와 거리 {long}km, 소요시간 {long}분으로 응답한다.", (String stations, Long distance, Long duration) -> {
            String[] stationArr = stations.replaceAll(" ", "").split(",");
            List<Long> stationIds = Arrays.stream(stationArr).map(s -> findStation(s).getId()).collect(Collectors.toList());

            assertThat(response.jsonPath().getList("stations.id", Long.class)).isEqualTo(stationIds);
            assertThat(response.jsonPath().getLong("distance")).isEqualTo(distance);
            assertThat(response.jsonPath().getLong("duration")).isEqualTo(duration);
        });
    }

    private void putStation(String name, ExtractableResponse<Response> response) {
        StationResponse responseObj = response.as(StationResponse.class);
        acceptanceContext.putStation(name, new Station(responseObj.getId(), responseObj.getName()));
    }

    private Station findStation(String name) {
        return acceptanceContext.findStation(name);
    }

    private Station findStationById(Long id) {
        return acceptanceContext.findStationById(id);
    }

    private void putLine(LineRequest request, ExtractableResponse<Response> lineResponse) {
        LineResponse responseObj = lineResponse.as(LineResponse.class);
        acceptanceContext.putLine(request.getName(), new Line(responseObj.getId(), responseObj.getName(), responseObj.getColor(), findStationById(request.getUpStationId()), findStationById(request.getDownStationId()), request.getDistance(), request.getDuration()));
    }

    private void addSection(String line, String upStation, String downStation, Long distance, Long duration) {
        Section newSection = new Section(findLine(line), findStation(upStation), findStation(downStation), distance, duration);
        acceptanceContext.addSection(line, newSection);
    }

    private Line findLine(String name) {
        return acceptanceContext.findLine(name);
    }
}
