package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import nextstep.subway.domain.Station;
import org.springframework.http.MediaType;

public class PathSteps {
  public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String type) {
    return RestAssured
        .given().log().all()
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> 두_역의_최단_거리_경로_유저로_조회를_요청(Long source, Long target, String type, String accessToken, RequestSpecification... spec) {
    if (Objects.nonNull(spec) && spec.length > 0){
      return RestAssured
          .given(spec[0]).log().all()
          .auth().preemptive().oauth2(accessToken)
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
          .then().log().all().extract();
    }
    return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
            .then().log().all().extract();
  }
  public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
    Map<String, String> lineCreateParams;
    lineCreateParams = new HashMap<>();
    lineCreateParams.put("name", name);
    lineCreateParams.put("color", color);
    lineCreateParams.put("upStationId", upStation + "");
    lineCreateParams.put("downStationId", downStation + "");
    lineCreateParams.put("distance", distance + "");
    lineCreateParams.put("duration", duration + "");

    return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
  }

  public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
    Map<String, String> params = new HashMap<>();
    params.put("upStationId", upStationId + "");
    params.put("downStationId", downStationId + "");
    params.put("distance", distance + "");
    params.put("duration", duration + "");
    return params;
  }

  public static void 역의_순서_검증(ExtractableResponse<Response> response, Long... stations) {
    assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stations);
  }
  public static void 경로_조회_거리_검증(ExtractableResponse<Response> response, Long distance) {
    assertThat(response.jsonPath().getLong("distance")).isEqualTo(distance);
  }
  public static void 경로_조회_시간_검증(ExtractableResponse<Response> response, Long duration) {
    assertThat(response.jsonPath().getLong("duration")).isEqualTo(duration);
  }
  public static void 경로_조회_요금_검증(ExtractableResponse<Response> response, int fare) {
    assertThat(response.jsonPath().getLong("fare")).isEqualTo(fare);
  }


}
