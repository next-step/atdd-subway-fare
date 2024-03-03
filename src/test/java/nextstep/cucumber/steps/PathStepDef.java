package nextstep.cucumber.steps;

import static nextstep.auth.acceptance.AuthSteps.토큰_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_구간_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.domain.PathSearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathStepDef implements En {

  @Autowired
  private AcceptanceContext context;

  public PathStepDef() {
    Given("지하철 역을 생성하고", (DataTable table) -> {
      table.asList().forEach(stationName -> {
        Long stationId = 지하철역_생성(stationName).getId();
        context.store.put(stationName, stationId);
      });
    });
    And("지하철 노선을 생성하고", (DataTable table) -> {
      for (Map<String, String> arguments : table.asMaps()) {
        String name = arguments.get("name");
        Long lineId = 지하철_노선_생성(
            arguments.get("name"),
            arguments.get("color"),
            context.getLong(arguments.get("upStationName")),
            context.getLong(arguments.get("downStationName")),
            Integer.parseInt(arguments.get("distance")),
            Integer.parseInt(arguments.get("duration"))
        ).getId();
        context.store.put(name, lineId);
      }
    });
    And("지하철 노선의 구간들을 생성하고", (DataTable table) -> {
      for (Map<String, String> arguments : table.asMaps()) {
        지하철_구간_생성_요청(
            context.getLong(arguments.get("name")),
            context.getLong(arguments.get("upStationName")),
            context.getLong(arguments.get("downStationName")),
            Integer.parseInt(arguments.get("distance")),
            Integer.parseInt(arguments.get("duration"))
        );
      }
    });
    And("지하철 승객 정보를 생성한다.", (DataTable table) -> {
      for (Map<String, String> arguments : table.asMaps()) {
        String email = arguments.get("email");
        String password = arguments.get("password");

        회원_생성_요청(email, password, Integer.parseInt(arguments.get("age")));

        String accessToken = 토큰_요청(email, password).as(TokenResponse.class).getAccessToken();
        context.store.put(arguments.get("alias"), accessToken);
      }
    });

    /**
     * 최단 거리 경로 조회 성공
     */
    When("{string}과 {string}의 최단 거리 경로를 조회하면", (String source, String target) -> {
      String accessToken = context.store.get("성인").toString();
      context.response = 경로_조회_요청(source, target, PathSearchType.DISTANCE, accessToken);
    });

    Then("두 역을 잇는 경로 중 거리가 가장 짧은 경로를 반환한다.", (DataTable table) -> {
      List<Long> expected = table.asLists(String.class).stream()
          .map(it -> it.get(0))
          .map(it -> context.getLong(it))
          .collect(Collectors.toList());

      assertThat(context.response.jsonPath().getList("stations.id", Long.class)).containsExactly(expected.toArray(new Long[0]));
    });
    And("거리가 가장 짧은 경로의 이동거리는 {int}km 소요시간은 {int}분이다.", (Integer distance, Integer duration) -> {
      assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(distance);
      assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(duration);
    });
    And("운임은 1250원이다.", () -> {
      assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(1250);
    });

    /**
     * 최단 시간 경로 조회 성공
     */
    When("{string}과 {string}의 최단 시간 경로를 조회하면", (String source, String target) -> {
      String accessToken = context.store.get("성인").toString();
      context.response = 경로_조회_요청(source, target, PathSearchType.DURATION, accessToken);
    });

    Then("두 역을 잇는 경로 중 소요시간이 가장 짧은 경로를 반환한다.", (DataTable table) -> {
      List<Long> expected = table.asLists(String.class).stream()
          .map(it -> it.get(0))
          .map(it -> context.getLong(it))
          .collect(Collectors.toList());

      assertThat(context.response.jsonPath().getList("stations.id", Long.class)).containsExactly(expected.toArray(new Long[0]));
    });
    And("시간이 제일 적게 소요되는 경로의 이동거리는 {int}km 소요시간은 {int}분이다.", (Integer distance, Integer duration) -> {
      assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(distance);
      assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(duration);
    });
    And("운임은 1350원이다.", () -> {
      assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(1350);
    });

    /**
     * 거리가 50km를 초과하는 구간의 경로 조회
     */
    When("거리가 50km를 초과하는 {string}과 {string}의 경로를 조회하면", (String source, String target) -> {
      String accessToken = context.store.get("성인").toString();
      context.response = 경로_조회_요청(source, target, PathSearchType.DURATION, accessToken);
    });
    Then("운임은 2150원이다.", () -> {
      assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(2150);
    });

    /**
     * 어린이 할인 적용
     */
    When("어린이가 {string}과 {string}의 최단 시간 경로를 조회하면", (String source, String target) -> {
      String accessToken = context.store.get("어린이").toString();
      context.response = 경로_조회_요청(source, target, PathSearchType.DURATION, accessToken);
    });
    Then("운임은 500원이다.", () -> {
      assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(900);
    });

    /**
     * 청소년 할인 적용
     */
    When("청소년이 {string}과 {string}의 경로를 조회하면", (String source, String target) -> {
      String accessToken = context.store.get("어린이").toString();
      context.response = 경로_조회_요청(source, target, PathSearchType.DURATION, accessToken);
    });
    Then("운임은 1440원이다.", () -> {
      assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(1440);
    });

    /**
     * 경로 조회 실패 - 인증 토큰 누락
     */
    When("인증되지 않은 사용자가 {string}과 {string}의 경로를 조회하면", (String source, String target) -> {
      context.response = 경로_조회_요청(source, target, PathSearchType.DISTANCE, "UNAUTHORIZED_TOKEN");
    });

    Then("인증 에러가 발생한다.", () -> {
      assertThat(context.response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    });

    /**
     * 경로 조회 실패 - 두 역 사이 경로가 존재하지 않음
     */
    When("{string}과 {string}의 경로를 조회하면", (String source, String target) -> {
      String accessToken = context.store.get("성인").toString();
      context.response = 경로_조회_요청(source, target, PathSearchType.DISTANCE, accessToken);
    });

    Then("에러가 발생한다.", () -> {
      assertThat(context.response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(context.response.body().asString()).isEqualTo("경로를 찾을 수 없습니다.");
    });
  }

  private ExtractableResponse<Response> 경로_조회_요청(
      String source,
      String target,
      PathSearchType pathSearchType,
      String accessToken
  ) {
    return RestAssured
        .given().log().all()
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .auth().oauth2(accessToken)
        .when().get(
            "/paths?source={sourceId}&target={targetId}&type={type}",
            context.store.get(source),
            context.store.get(target),
            pathSearchType.name()
        )
        .then().log().all().extract();
  }
}
