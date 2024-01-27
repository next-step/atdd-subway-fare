package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {

    @Autowired
    private AcceptanceContext acceptanceContext;

    public PathStepDef() {
        Given("지하철 역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                String name = map.get("name");
                Long stationId = 지하철역_생성_요청(name).jsonPath().getLong("id");
                acceptanceContext.store.put(name, stationId);
            }
        });

        Given("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            for (Map<String, String> map : table.asMaps()) {
                String name = map.get("name");
                String color = map.get("color");
                String upStationName = map.get("upStationName");
                String downStationName = map.get("downStationName");
                int distance = Integer.parseInt(map.get("distance"));

                Long lineId = 지하철_노선_생성_요청(name, color, (Long) acceptanceContext.store.get(upStationName), (Long) acceptanceContext.store.get(downStationName), distance);
                acceptanceContext.store.put(name, lineId);
            }
        });

        Given("지하철 노선에 지하철 구간 생성 요청하고", (DataTable table) -> {
            for (Map<String, String> map : table.asMaps()) {
                String name = map.get("name");
                String upStationName = map.get("upStationName");
                String downStationName = map.get("downStationName");
                int distance = Integer.parseInt(map.get("distance"));

                지하철_노선에_지하철_구간_생성_요청((Long) acceptanceContext.store.get(name), createSectionCreateParams((Long) acceptanceContext.store.get(upStationName), (Long) acceptanceContext.store.get(downStationName), distance));
            }
        });

        When("{string}과 {string}의 최단 거리 경로를 조회하면", (String source, String target) -> {
            acceptanceContext.response = RestAssured
                    .given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/paths?source={sourceId}&target={targetId}", acceptanceContext.store.get(source), acceptanceContext.store.get(target))
                    .then().log().all().extract();
        });

        Then("최단 거리 경로를 응답 받는다", (DataTable table) -> {
            List<List<String>> rows = table.asLists(String.class);

            List<Long> expected = rows.stream()
                    .map(it -> it.get(0))
                    .map(it -> (Long) acceptanceContext.store.get(it))
                    .collect(Collectors.toList());

            assertThat(acceptanceContext.response.jsonPath().getList("stations.id", Long.class)).containsExactly(expected.toArray(new Long[0]));
        });
    }

}
