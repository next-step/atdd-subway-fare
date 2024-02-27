package nextstep.cucumber.steps;

import static nextstep.subway.acceptance.LineSteps.지하철_구간_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class PathStepDef implements En {

  @Autowired
  private AcceptanceContext context;

  public PathStepDef() {
    Given("지하철 역을 생성하고", (DataTable table) -> {
      List<Map<String, String>> maps = table.asMaps();
      for (Map<String, String> map : maps) {
        String name = map.get("name");
        Long stationId = 지하철역_생성(name).getId();
        context.store.put(name, stationId);
      }
    });

    Given("지하철 노선을 생성하고", (DataTable table) -> {
      for (Map<String, String> map : table.asMaps()) {
        String name = map.get("name");
        String color = map.get("color");
        String upStationName = map.get("upStationName");
        String downStationName = map.get("downStationName");
        int distance = Integer.parseInt(map.get("distance"));

        Long lineId = 지하철_노선_생성(name, color, (Long) context.store.get(upStationName), (Long) context.store.get(downStationName), distance).getId();
        context.store.put(name, lineId);
      }
    });

    Given("지하철 노선의 구간들을 생성하고", (DataTable table) -> {
      for (Map<String, String> map : table.asMaps()) {
        String name = map.get("name");
        String upStationName = map.get("upStationName");
        String downStationName = map.get("downStationName");
        int distance = Integer.parseInt(map.get("distance"));

        지하철_구간_생성_요청((Long) context.store.get(name), (Long) context.store.get(upStationName), (Long) context.store.get(downStationName), distance);
      }
    });

    When("{string}과 {string}의 경로를 조회하면", (String source, String target) -> {
      context.response = RestAssured
          .given().log().all()
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .when().get("/paths?source={sourceId}&target={targetId}", context.store.get(source), context.store.get(target))
          .then().log().all().extract();
    });

    Then("두 역을 잇는 가장 짧은 경로를 반환한다.", (DataTable table) -> {
      List<List<String>> rows = table.asLists(String.class);

      List<Long> expected = rows.stream()
          .map(it -> it.get(0))
          .map(it -> (Long) context.store.get(it))
          .collect(Collectors.toList());

      assertThat(context.response.jsonPath().getList("stations.id", Long.class)).containsExactly(expected.toArray(new Long[0]));
    });
  }
}
