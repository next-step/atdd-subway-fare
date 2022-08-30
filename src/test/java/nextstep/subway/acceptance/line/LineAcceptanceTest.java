package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_TIME;
import static nextstep.subway.acceptance.line.LineSteps.지하철_노선_검증;
import static nextstep.subway.acceptance.line.LineSteps.지하철_노선_목록_조회_요청;
import static nextstep.subway.acceptance.line.LineSteps.지하철_노선_삭제_요청;
import static nextstep.subway.acceptance.line.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.line.LineSteps.지하철_노선_수정_요청;
import static nextstep.subway.acceptance.line.LineSteps.지하철_노선_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(관리자, "2호선", "green");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ExtractableResponse<Response> listResponse = 지하철_노선_목록_조회_요청();

        assertThat(listResponse.jsonPath().getList("name")).contains("2호선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        지하철_노선_생성_요청(관리자, "2호선", "green");
        지하철_노선_생성_요청(관리자, "3호선", "orange");

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).contains("2호선", "3호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(관리자, "2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(관리자, "2호선", "green");

        // when
        지하철_노선_수정_요청(관리자, createResponse.header("location"));

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("color")).isEqualTo("red");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(관리자, "2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(관리자, createResponse.header("location"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * When 지하철 노선 정보(첫차 시간, 막차 시간, 간격)을 추가하면
     * Then 지하철 노선 정보가 추가된다.
     * When 추가된 지하철 노선을 조회하면
     * Then 추가된 지하철 노선 정보가 조회된다.
     * When 추가된 지하철 노선의 첫차 시간, 막차 시간, 간격을 수정하면
     * Then 지하철 노선의 정보가 수정된다.
     * When 추가된 지하철 노선을 삭제하면
     * Then 지하철 노선이 삭제된다.
     */
    @DisplayName("지하철 노선 첫차, 막차, 간격 관리")
    @Test
    void saveLineWithTimes() {
        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(관리자, "2호선", "green", LocalTime.of(05, 00, 00), LocalTime.of(23, 00, 00), 10);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> findResponse = 지하철_노선_조회_요청(createResponse);

        // then
        지하철_노선_검증(findResponse, "2호선", "green", "05:00:00", "23:00:00", 10);

        // when
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(관리자, createResponse, "red", LocalTime.of(06, 00, 00), LocalTime.of(23, 30, 00), 5);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> afterUpdateResponse = 지하철_노선_조회_요청(createResponse);
        지하철_노선_검증(afterUpdateResponse, "2호선", "red", "06:00:00", "23:30:00", 5);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제_요청(관리자, createResponse);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
