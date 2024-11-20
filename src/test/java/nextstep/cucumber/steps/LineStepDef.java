package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.acceptance.LineCommonApi;
import nextstep.subway.domain.line.dto.LineCreateRequest;
import nextstep.subway.domain.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class LineStepDef implements En {

  @Autowired
  private AcceptanceContext context;

  @Given("지하철 노선을 생성하고")
  public void 지하철역_노선_생성_요청(DataTable dataTable) {
    List<Map<String, String>> maps = dataTable.asMaps();

    maps.forEach(e -> {
      var request = new LineCreateRequest(e.get("name"),
          e.get("color"),
          ((StationResponse) context.store.get(e.get("upStation"))).getId(),
          ((StationResponse) context.store.get(e.get("downStation"))).getId(),
          Integer.parseInt(e.get("distance")),
          Integer.parseInt(e.get("transitTime")),
          Long.parseLong(e.get("additionalFare")));
      var lineId = LineCommonApi.createLine(request).jsonPath().getLong("id");
      context.store.put(e.get("name"), lineId);
    });
  }
}
