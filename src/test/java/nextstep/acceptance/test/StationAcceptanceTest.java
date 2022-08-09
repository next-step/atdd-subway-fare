package nextstep.acceptance.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.acceptance.steps.LineSteps.*;
import static nextstep.acceptance.steps.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var response = 지하철역_생성_요청(관리자, 강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        지하철역들이_존재한다(강남역);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // when
        지하철역_생성_요청(관리자, 강남역);
        지하철역_생성_요청(관리자, 역삼역);

        // then
        지하철역들이_존재한다(강남역, 역삼역);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Long 강남 = 지하철역_생성_요청(관리자, 강남역).jsonPath().getLong("id");

        // when
        지하철역_삭제_요청(관리자, 강남);

        // then
        지하철역들이_존재한다();
    }
    
    @DisplayName("노선에 포함된 지하철역은 제거할 수 없다.")
    @Test
    void deleteStation_Exception() {
        // given
        Long 역삼 = 지하철역_생성_요청(관리자, 역삼역).jsonPath().getLong("id");
        Long 강남 = 지하철역_생성_요청(관리자, 강남역).jsonPath().getLong("id");
        Long 신분당선 = 지하철_노선_생성_요청(관리자, "신분당선", "green").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(역삼, 강남));

        // when
        var deleteResponse = 지하철역_삭제_요청(관리자, 강남);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        지하철역들이_존재한다(역삼역, 강남역);
    }
}