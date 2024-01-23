package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext acceptanceContext;
    private ExtractableResponse<Response> response;
    public PathStepDef() {
        Given("지하철역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (String name : maps.stream().map(it -> it.get("name")).collect(Collectors.toList())) {
                Long id = 지하철역_생성_요청(name).jsonPath().getLong("id");
                acceptanceContext.put(name, id);
            }
        });
        Given("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                String name = map.get("name");
                String color = map.get("color");
                String upStationName = map.get("upStationName");
                String downStationName = map.get("downStationName");
                String distance = map.get("distance");
                Long id = 지하철_노선_생성_요청(name, color, (Long) acceptanceContext.get(upStationName), (Long) acceptanceContext.get(downStationName), Integer.parseInt(distance));

                acceptanceContext.put(name, id);
            }
        });
        Given("{string} 노선에 지하철 {string} {string} 구간을 거리 {int}으로 생성 요청하고", (String lineName, String upStationName, String downStationName, Integer distance) -> {
            Long lineId = (Long) acceptanceContext.get(lineName);
            Long upStationId = (Long) acceptanceContext.get(upStationName);
            Long downStationId = (Long) acceptanceContext.get(downStationName);
            지하철_노선에_지하철_구간_생성_요청(lineId, createSectionCreateParams(upStationId, downStationId, distance));
        });
        When("{string}과 {string}의 경로를 조회하면", (String sourceName, String targetName) -> {
            Long sourceId = (Long) acceptanceContext.get(sourceName);
            Long targetId = (Long) acceptanceContext.get(targetName);
            response = RestAssured
                    .given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                    .then().log().all().extract();
        });

        Then("두 역의 최단 경로를 찾을 수 있다", (DataTable table) -> {
            List<Long> stationIds = table.asList().stream().map(name -> (Long) acceptanceContext.get(name)).collect(Collectors.toList());
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactlyElementsOf(stationIds);
        });

    }
}
