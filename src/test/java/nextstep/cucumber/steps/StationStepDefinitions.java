package nextstep.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.support.AcceptanceContext;
import nextstep.subway.station.application.dto.StationRequest;
import nextstep.subway.station.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  @When("지하철역을 생성하면")
  public void 지하철역을_생성하면() {
    Map<String, String> params = new HashMap<>();
    params.put("name", "강남역");
    context.response =
        RestAssured.given()
            .log()
            .all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then()
            .log()
            .all()
            .extract();
  }

  @Then("지하철역이 생성된다")
  public void 지하철역이_생성된다() {
    assertThat(context.response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다")
  public void 지하철역_목록_조회_시_생성한_역을_찾을_수_있다() {
    List<String> stationNames =
        RestAssured.given()
            .log()
            .all()
            .when()
            .get("/stations")
            .then()
            .log()
            .all()
            .extract()
            .jsonPath()
            .getList("name", String.class);
    assertThat(stationNames).containsAnyOf("강남역");
  }
}
