package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {

    private Map<String, Long> stations = new HashMap<>();
    private Map<String, Long> lines = new HashMap<>();
    ExtractableResponse<Response> response;

    public PathStepDef() {
        Given("지하철 역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                String name = map.get("name");
                Long stationId = 지하철역_생성_요청(name).jsonPath().getLong("id");
                stations.put(name, stationId);
            }
        });

        Given("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            for (Map<String, String> map : table.asMaps()) {
                String name = map.get("name");
                String color = map.get("color");
                String upStationName = map.get("upStationName");
                String downStationName = map.get("downStationName");
                int distance = Integer.parseInt(map.get("distance"));

                Long lineId = 지하철_노선_생성_요청(name, color, stations.get(upStationName), stations.get(downStationName), distance);
                lines.put(name, lineId);
            }
        });

        Given("지하철 노선에 지하철 구간 생성 요청하고", (DataTable table) -> {
            for (Map<String, String> map : table.asMaps()) {
                String name = map.get("name");
                String upStationName = map.get("upStationName");
                String downStationName = map.get("downStationName");
                int distance = Integer.parseInt(map.get("distance"));

                지하철_노선에_지하철_구간_생성_요청(lines.get(name), createSectionCreateParams(stations.get(upStationName), stations.get(downStationName), distance));
            }
        });

        When("두 역의 최단 거리 경로를 조회하면", () -> {
            response = RestAssured
                    .given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/paths?source={sourceId}&target={targetId}", stations.get("교대역"), stations.get("양재역"))
                    .then().log().all().extract();
        });

        Then("최단 거리 경로를 응답 받는다", () -> {
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stations.get("교대역"), stations.get("남부터미널역"), stations.get("양재역"));
        });
    }

}
