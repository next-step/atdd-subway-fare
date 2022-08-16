package nextstep.acceptance.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.acceptance.steps.LineSectionSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        var response = 지하철_노선_생성_요청(관리자, "2호선", "green");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        지하철_노선들이_존재한다("2호선");
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        지하철_노선_생성_요청(관리자, "2호선", "green");
        지하철_노선_생성_요청(관리자, "3호선", "orange");

        // when + then
        지하철_노선들이_존재한다("2호선", "3호선");
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        var createResponse = 지하철_노선_생성_요청(관리자, "2호선", "green", 900);

        // when
        var response = 지하철_노선_조회_요청(createResponse);

        // then
        지하철_노선_정보가_일치한다(response, "2호선", "green", 900);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        var createResponse = 지하철_노선_생성_요청(관리자, "2호선", "green");

        // when
        지하철_노선_수정_요청(관리자,
                createResponse.header("location"),
                createLineUpdateParams("신2호선", "green", 800));

        // then
        var response = 지하철_노선_조회_요청(createResponse);
        지하철_노선_정보가_일치한다(response, "신2호선", "green", 800);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        var createResponse = 지하철_노선_생성_요청(관리자, "2호선", "green");

        // when
        var deleteResponse = 지하철_노선_삭제_요청(관리자, createResponse.header("location"));

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        지하철_노선들이_존재한다();
    }

    private void 지하철_노선_정보가_일치한다(ExtractableResponse<Response> response, String name, String color, int extraFare) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.jsonPath().getString("name")).isEqualTo(name);
        assertThat(response.jsonPath().getString("color")).isEqualTo(color);
        assertThat(response.jsonPath().getInt("extraFare")).isEqualTo(extraFare);
    }

    private void 지하철_노선들이_존재한다(String... names) {
        var listResponse = 지하철_노선_목록_조회_요청();

        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(listResponse.jsonPath().getList("name")).containsExactlyInAnyOrder(names);
    }
}
