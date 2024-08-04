package nextstep.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import java.util.List;
import nextstep.cucumber.support.AcceptanceContext;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class PathStepDefinitions {
  @Autowired private AcceptanceContext context;

  @When("{string}에서 {string}까지 최단 거리 경로를 조회하면")
  public void 교대역_에서_강남역_까지_최단_거리_경로를_조회하면(String source, String target) {
    Long sourceId = ((StationResponse) context.store.get(source)).getId();
    Long targetId = ((StationResponse) context.store.get(target)).getId();
    context.response =
        RestAssured.given()
            .log()
            .all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParams("source", sourceId, "target", targetId, "type", PathType.DISTANCE.name())
            .when()
            .get("/paths")
            .then()
            .log()
            .all()
            .extract();
  }

  @When("{string}에서 {string}까지 최소 시간 경로를 조회하면")
  public void 교대역_에서_양재역_까지_최소_시간_경로를_조회하면(String source, String target) {
    Long sourceId = ((StationResponse) context.store.get(source)).getId();
    Long targetId = ((StationResponse) context.store.get(target)).getId();
    context.response =
        RestAssured.given()
            .log()
            .all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParams("source", sourceId, "target", targetId, "type", PathType.DURATION.name())
            .when()
            .get("/paths")
            .then()
            .log()
            .all()
            .extract();
  }

  @Then("{string} 경로가 조회된다")
  public void 경로가_조회된다(String pathString) {
    List<String> expectedNames = List.of(pathString.split(","));
    List<String> actualNames = context.response.jsonPath().getList("stations.name", String.class);
    assertThat(actualNames).containsExactlyElementsOf(expectedNames);
  }

  @Then("총 거리는 {int}km이며 총 소요 시간은 {int}분이다")
  public void 총_거리는_x_km이며_총_소요_시간은_x_분이다(int distance, int duration) {
    long actualDistance = context.response.jsonPath().getLong("distance");
    long actualDuration = context.response.jsonPath().getLong("duration");
    assertThat(actualDistance).isEqualTo(distance);
    assertThat(actualDuration).isEqualTo(duration);
  }
}
