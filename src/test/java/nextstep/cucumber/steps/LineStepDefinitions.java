package nextstep.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.support.AcceptanceContext;
import nextstep.subway.line.application.dto.*;
import nextstep.subway.station.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class LineStepDefinitions {
  @Autowired private AcceptanceContext context;

  @Given("구간들을 등록하고")
  public void 구간들을_등록하고(List<Map<String, String>> rows) {
    rows.forEach(
        it -> {
          Long upStationId = ((StationResponse) context.store.get(it.get("upStation"))).getId();
          Long downStationId = ((StationResponse) context.store.get(it.get("downStation"))).getId();
          LineSectionRequest request =
              new LineSectionRequest(
                  upStationId,
                  downStationId,
                  Integer.parseInt(it.get("distance")),
                  Integer.parseInt(it.get("duration")));
          LineResponse line = (LineResponse) context.store.get(it.get("line"));
          RestAssured.given()
              .log()
              .all()
              .contentType(MediaType.APPLICATION_JSON_VALUE)
              .body(request)
              .when()
              .post("/lines/" + line.getId() + "/sections")
              .then()
              .log()
              .all()
              .extract();
        });
  }

  @When("노선들을 생성하고")
  public void 노선들을_생성하고(List<Map<String, String>> rows) {
    rows.forEach(
        row -> {
          Long upStationId = ((StationResponse) context.store.get(row.get("upStation"))).getId();
          Long downStationId =
              ((StationResponse) context.store.get(row.get("downStation"))).getId();
          LineRequest request =
              LineRequest.builder()
                  .name(row.get("name"))
                  .color(row.get("color"))
                  .upStationId(upStationId)
                  .downStationId(downStationId)
                  .distance(Integer.parseInt(row.get("distance")))
                  .duration(Integer.parseInt(row.get("duration")))
                  .build();
          var response =
              RestAssured.given()
                  .log()
                  .all()
                  .body(request)
                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                  .when()
                  .post("/lines")
                  .then()
                  .log()
                  .all()
                  .extract();
          context.store.put(request.getName(), response.as(LineResponse.class));
        });
  }

  @Then("지하철 노선 목록 조회 시 {string}을 찾을 수 있다")
  public void 지하철_노선_목록_조회_시_생성한_노선을_찾을_수_있다(String line) {
    var response =
        RestAssured.given().log().all().when().get("/lines").then().log().all().extract();
    List<LineResponse> actualLines = response.jsonPath().getList(".", LineResponse.class);
    LineResponse expectedLine = (LineResponse) context.store.get(line);
    assertThat(actualLines).contains(expectedLine);
  }
}
