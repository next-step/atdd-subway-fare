package nextstep.subway.acceptance.line.steps;

import static nextstep.subway.acceptance.line.steps.LineAcceptanceSteps.지하철_노선_조회_요청2;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.line.application.dto.LineSectionRequest2;
import nextstep.subway.line.domain.Line2;
import nextstep.subway.line.domain.LineSection2;
import nextstep.subway.station.domain.Station;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class LineSectionAcceptanceSteps2 {
  private LineSectionAcceptanceSteps2() {}

  public static ExtractableResponse<Response> 노선_구간_등록_요청2(Line2 line, LineSection2 lineSection) {
    LineSectionRequest2 request =
        new LineSectionRequest2(
            lineSection.getUpStation().getId(),
            lineSection.getDownStation().getId(),
            lineSection.getDistance(),
            lineSection.getDuration());
    return RestAssured.given()
        .log()
        .all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(request)
        .when()
        .post("/new/lines/" + line.getId() + "/sections")
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 노선_첫_구간으로_등록됨2(
      ExtractableResponse<Response> response, Line2 line, LineSection2 lineSection) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청2("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds.get(0)).isEqualTo(lineSection.getDownStation().getId());
  }

  public static void 노선_마지막_구간으로_등록됨2(
      ExtractableResponse<Response> response, Line2 line, LineSection2 lineSection) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청2("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds.get(stationIds.size() - 1))
        .isEqualTo(lineSection.getDownStation().getId());
  }

  public static void 노선_i변째_구간으로_등록됨2(
      ExtractableResponse<Response> response, Line2 line, LineSection2 lineSection, int i) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청2("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds.get(i)).isEqualTo(lineSection.getDownStation().getId());
  }

  public static void 노선_구간_요청_실패함2(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  public static ExtractableResponse<Response> 노선_구간_삭제_요청2(Line2 line, Station station) {
    String uri = String.format("/new/lines/%d/sections", line.getId());
    return RestAssured.given()
        .log()
        .all()
        .queryParam("stationId", station.getId())
        .when()
        .delete(uri)
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 노선_구간_삭제됨2(
      ExtractableResponse<Response> response, Line2 line, Station station) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청2("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds).isNotEmpty().doesNotContain(station.getId());
  }

  public static void 노선_구간_삭제_실패함2(ExtractableResponse<Response> response, HttpStatus httpStatus) {
    assertThat(response.statusCode()).isEqualTo(httpStatus.value());
  }
}
