package nextstep.cucumber.steps;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import java.util.List;
import nextstep.cucumber.support.AcceptanceContext;
import nextstep.subway.station.application.dto.StationRequest;
import nextstep.subway.station.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class StationStepDefinitions {
  @Autowired private AcceptanceContext context;

  @Given("지하철역들을 생성하고")
  public void 지하철역들을_생성하고(List<StationRequest> stationRequests) {
    stationRequests.forEach(
        request -> {
          var response =
              RestAssured.given()
                  .log()
                  .all()
                  .body(request)
                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                  .when()
                  .post("/stations")
                  .then()
                  .log()
                  .all()
                  .extract();
          context.store.put(request.getName(), response.as(StationResponse.class));
        });
  }
}
