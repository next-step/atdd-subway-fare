package nextstep.cucumber.steps;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.support.AcceptanceContext;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.LineSectionRequest;
import nextstep.subway.station.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class LineStepDefinitions {
  @Autowired private AcceptanceContext context;

  @Given("노선들을 생성하고")
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

  @Given("구간들을 등록하고")
  public void 구간들을_등록하고(List<Map<String, String>> rows) {
    rows.forEach(
        it -> {
          Long upStationId = ((StationResponse) context.store.get(it.get("upStation"))).getId();
          Long downStationId = ((StationResponse) context.store.get(it.get("downStation"))).getId();
          LineSectionRequest request =
              new LineSectionRequest(
                  upStationId, downStationId, Integer.parseInt(it.get("distance")));
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
}
