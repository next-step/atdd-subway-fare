package nextstep.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java8.En;
import io.restassured.path.json.JsonPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.member.acceptance.MemberSteps;
import nextstep.subway.acceptance.PathCommonApi;
import nextstep.subway.domain.path.dto.PathResponse;
import nextstep.subway.domain.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class PathStepDef implements En {

  @Autowired
  private AcceptanceContext context;

  public PathStepDef() {
  }

  @When("{string}과 {string}의 경로 탐색을 수행하면")
  public void 정상적인_지하철역_경로_탐색을_수행하면(String upStation, String downStation) {
    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();

    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE");
  }

  @Then("지하철역 경로 탐색 결과가 반환된다.")
  public void 지하철역_경로_탐색_결과가_반환된다() {
    JsonPath jsonPath = context.response.jsonPath();
    List<String> list = context.response.jsonPath().getList("stations.name", String.class);
    assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly("남부터미널역", "양재역", "강남역", "역삼역");
    assertThat(context.response.jsonPath().getLong("distance")).isEqualTo(39);
  }

  @When("{string}을 출발역과 도착역으로 하여 경로 탐색을 수행하면")
  public void 같은_역을_출발역과_도착역으로_하여_경로_탐색을_수행하면(String sameStation) {
    Long sameStationId = ((StationResponse) context.store.get(sameStation)).getId();
    context.response = PathCommonApi.findLinePath(sameStationId, sameStationId, "DISTANCE");
  }

  @Then("지하철역 경로 탐색에 실패한다.")
  public void 지하철역_경로_탐색에_실패한다() {
    assertThat(context.response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @When("연결되지 않은 {string}과 {string}에 대해 경로 탐색을 수행하면")
  public void 연결되지_않은_두_역에_대해_경로_탐색을_수행하면(String upStation, String downStation) {
    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(upStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE");
  }

  @When("등록되지 않은 {string}과 존재하는 {string}에 대하여 경로 탐색을 수행하면")
  public void 존재하지_않는_역에_대하여_경로_탐색을_수행하면(String upStation, String downStation) {
    Long upStationId = 12312L;
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE");
  }

  @When("{string}에서 {string}까지의 최소 시간 기준으로 경로 조회를 요청")
  public void 출발역에서_도착역까지의_최소_시간_기준_경로_조회(String upStation, String downStation) {
    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DURATION");
  }

  @When("{string}에서 {string}까지의 최단 거리 경로 조회를 요청")
  public void 출발역에서_도착역까지의_최단_거리_기준_경로_조회(String upStation, String downStation) {
    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE");
  }

  @When("청소년 사용자가 {string}에서 {string}까지의 최단 거리 경로 조회를 요청")
  public void 청소년_사용자_출발역에서_도착역까지의_최단_거리_기준_경로_조회(String upStation, String downStation) {
    String email = "testemail1@email.com";
    String password = "1234";

    var token = "";
    if (!email.isBlank() && !password.isBlank()) {
      Map<String, Object> params = new HashMap<>();
      params.put("email", email);
      params.put("password", password);
      token = "Bearer " + MemberSteps.회원_로그인_요청(params).jsonPath().getString("accessToken");
    }

    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE", token);
  }

  @When("어린이 사용자가 {string}에서 {string}까지의 최단 거리 경로 조회를 요청")
  public void 어린이_사용자_출발역에서_도착역까지의_최단_거리_기준_경로_조회(String upStation, String downStation) {
    String email = "testemail@email.com";
    String password = "1234";

    var token = "";
    if (!email.isBlank() && !password.isBlank()) {
      Map<String, Object> params = new HashMap<>();
      params.put("email", email);
      params.put("password", password);
      token = "Bearer " + MemberSteps.회원_로그인_요청(params).jsonPath().getString("accessToken");
    }

    Long upStationId = ((StationResponse) context.store.get(upStation)).getId();
    Long downStationId = ((StationResponse) context.store.get(downStation)).getId();
    context.response = PathCommonApi.findLinePath(upStationId, downStationId, "DISTANCE", token);
  }


  @Then("최소 시간 기준 경로를 응답")
  public void 최소_시간_경로_응답(DataTable dataTable) {
    Map<String, String> data = dataTable.asMap();
    assertThat(context.response.jsonPath().getLong("transitTime")).isEqualTo(Long.parseLong(data.get("transitTime")));
  }

  @Then("최단 거리 기준 경로를 응답")
  public void 최단_거리_경로_응답(DataTable dataTable) {
    Map<String, String> data = dataTable.asMap();
    assertThat(context.response.jsonPath().getLong("distance")).isEqualTo(Long.parseLong(data.get("distance")));
  }

  @Then("총 거리와 소요 시간을 함께 응답함")
  public void 총_거리_및_소요_시간을_함께_응답() {
    PathResponse pathResponse = context.response.as(PathResponse.class);
    assertThat(pathResponse.getDistance()).isNotNull();
    assertThat(pathResponse.getTransitTime()).isNotNull();
  }

  @Then("지하철 이용 요금도 함께 응답함")
  public void 지하철_이용_요금도_함께_응답(DataTable dataTable) {
    Map<String, String> data = dataTable.asMap();
    PathResponse pathResponse = context.response.as(PathResponse.class);
    assertThat(pathResponse.getFare()).isNotNull();
    assertThat(pathResponse.getFare()).isEqualTo(Long.parseLong(data.get("fare")));
  }

  @Then("20퍼센트 할인된 요금도 함께 응답함")
  public void 청소년_할인_요금도_함께_응답(DataTable dataTable) {
      Map<String, String> data = dataTable.asMap();
      PathResponse pathResponse = context.response.as(PathResponse.class);
      assertThat(pathResponse.getFare()).isNotNull();
      assertThat(pathResponse.getFare()).isEqualTo(Long.parseLong(data.get("fare")));
  }

  @Then("50퍼센트 할인된 요금도 함께 응답함")
  public void 어린이_할인_요금도_함께_응답함(DataTable dataTable) {
      Map<String, String> data = dataTable.asMap();
      PathResponse pathResponse = context.response.as(PathResponse.class);
      assertThat(pathResponse.getFare()).isNotNull();
      assertThat(pathResponse.getFare()).isEqualTo(Long.parseLong(data.get("fare")));
  }
}