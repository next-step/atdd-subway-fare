package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.step.LineSteps.*;
import static nextstep.subway.acceptance.step.StationStepFeature.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private final int DISTANCE = 100;
    private final int DURATION = 10;
    private StationResponse 강남역;
    private StationResponse 판교역;
    private StationResponse 정자역;
    private StationResponse 미금역;
    private Map<String, String> params;
    private String 구분당선;

    @BeforeEach
    void setUpStation() {
        강남역 = 지하철역_생성_조회_요청(강남역_이름);
        판교역 = 지하철역_생성_조회_요청(판교역_이름);
        정자역 = 지하철역_생성_조회_요청(정자역_이름);
        미금역 = 지하철역_생성_조회_요청(미금역_이름);
        params = 노선_생성_Param_생성(신분당선_이름, 신분당선_색,
                강남역.getId(), 정자역.getId(),
                DISTANCE, DURATION);
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        // then
        노선_생성_응답상태_검증(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        String 구분당선 = "구분당선";
        지하철_노선_생성_요청(신분당선_이름, "red", 강남역.getId(), 판교역.getId());
        지하철_노선_생성_요청(구분당선, "orange", 정자역.getId(), 미금역.getId());

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        노선_조회_응답상태_검증(response);
        assertThat(response.jsonPath().getList("name")).contains(신분당선_이름, 구분당선);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선_이름, "red", 강남역.getId(), 판교역.getId());

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);

        // then
        노선_조회_응답상태_검증(response);
        assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선_이름);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선_이름, "red", 강남역.getId(), 정자역.getId());

        // when
        Map<String, String> params = new HashMap<>();
        params.put("color", "red");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(createResponse.header("location"))
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선_이름, "red", 강남역.getId(), 정자역.getId());

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete(createResponse.header("location"))
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicateName() {
        // given
        지하철_노선_생성_요청(신분당선_이름, "red", 강남역.getId(), 정자역.getId());

        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선_이름, "red", 강남역.getId(), 정자역.getId());

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
