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
import org.springframework.http.HttpStatus;
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

    And("지하철 노선을 생성하고", (DataTable table) -> {
      for (Map<String, String> map : table.asMaps()) {
        String name = map.get("name");
        String color = map.get("color");
        String upStationName = map.get("upStationName");
        String downStationName = map.get("downStationName");
        int distance = Integer.parseInt(map.get("distance"));
        int duration = Integer.parseInt(map.get("duration"));

        Long lineId = 지하철_노선_생성(
            name,
            color,
            context.getLong(upStationName),
            context.getLong(downStationName),
            distance,
            duration
        ).getId();
        context.store.put(name, lineId);
      }
    });

    And("지하철 노선의 구간들을 생성한다", (DataTable table) -> {
      for (Map<String, String> map : table.asMaps()) {
        String name = map.get("name");
        String upStationName = map.get("upStationName");
        String downStationName = map.get("downStationName");
        int distance = Integer.parseInt(map.get("distance"));
        int duration = Integer.parseInt(map.get("duration"));

        지하철_구간_생성_요청(
            context.getLong(name),
            context.getLong(upStationName),
            context.getLong(downStationName),
            distance,
            duration
        );
      }
    });

    /**
     * 최단 거리 경로 조회 성공
     */
    When("{string}과 {string}의 최단 거리 경로를 조회하면", (String source, String target) -> {
      context.response = RestAssured
          .given().log().all()
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .when().get(
              "/paths?source={sourceId}&target={targetId}&type={type}",
              context.store.get(source),
              context.store.get(target),
              "DISTANCE"  // TODO enum
          )
          .then().log().all().extract();
    });

    Then("두 역을 잇는 경로 중 거리가 가장 짧은 경로를 반환한다.", (DataTable table) -> {
      List<List<String>> rows = table.asLists(String.class);

      List<Long> expected = rows.stream()
          .map(it -> it.get(0))
          .map(it -> context.getLong(it))
          .collect(Collectors.toList());

      assertThat(context.response.jsonPath().getList("stations.id", Long.class)).containsExactly(expected.toArray(new Long[0]));
    });

    /**
     * 최단 시간 경로 조회 성공
     */
    When("{string}과 {string}의 최단 시간 경로를 조회하면", (String source, String target) -> {
      context.response = RestAssured
          .given().log().all()
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .when().get(
              "/paths?source={sourceId}&target={targetId}&type={type}",
              context.store.get(source),
              context.store.get(target),
              "DURATION"  // TODO enum
          )
          .then().log().all().extract();
    });

    Then("두 역을 잇는 경로 중 소요시간이 가장 짧은 경로를 반환한다.", (DataTable table) -> {
      List<List<String>> rows = table.asLists(String.class);

      List<Long> expected = rows.stream()
          .map(it -> it.get(0))
          .map(it -> context.getLong(it))
          .collect(Collectors.toList());

      assertThat(context.response.jsonPath().getList("stations.id", Long.class)).containsExactly(expected.toArray(new Long[0]));
    });

    /**
     * 경로 조회 실패 - 두 역 사이 경로가 존재하지 않음
     */
    When("{string}과 {string}의 경로를 조회하면", (String source, String target) -> {
      context.response = RestAssured
          .given().log().all()
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .when().get(
              "/paths?source={sourceId}&target={targetId}&type={type}",
              context.store.get(source),
              context.store.get(target),
              "DURATION"  // TODO enum
          )
          .then().log().all().extract();
    });

    Then("에러가 발생한다.", (DataTable table) -> {
      assertThat(context.response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(context.response.body().asString()).isEqualTo("경로를 찾을 수 없습니다.");
    });
  }
}
