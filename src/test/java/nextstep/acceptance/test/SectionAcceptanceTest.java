package nextstep.acceptance.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.acceptance.steps.LineSectionSteps.*;
import static nextstep.acceptance.steps.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {
    private Long 신분당선;

    private Long 강남역;
    private Long 양재역;

    /**
     * SETUP
     *
     * 신분당선에 노선을 등록한다
     * 강남역 >>> 양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역, 100);
        신분당선 = 지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

    @DisplayName("지하철 노선에 구간을 등록")
    @Test
    void addLineSection() {
        // when
        Long 정자역 = 지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(양재역, 정자역));

        // then
        노선에_역들이_순서대로_존재한다(신분당선, 강남역, 양재역, 정자역);
    }

    @DisplayName("지하철 노선 가운데에 구간을 추가")
    @Test
    void addLineSectionMiddle() {
        // when
        Long 정자역 = 지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(강남역, 정자역));

        // then
        노선에_역들이_순서대로_존재한다(신분당선, 강남역, 정자역, 양재역);
    }

    @DisplayName("이미 존재하는 구간을 추가")
    @Test
    void addSectionAlreadyIncluded() {
        // when
        var response = 지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(강남역, 양재역));

        // then
        요청이_실패한다(response);
    }

    @DisplayName("지하철 노선의 마지막 구간을 제거")
    @Test
    void removeLineSection() {
        // given
        Long 정자역 = 지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(양재역, 정자역));

        // when
        지하철_노선에_지하철_구간_제거_요청(관리자, 신분당선, 정자역);

        // then
        노선에_역들이_순서대로_존재한다(신분당선, 강남역, 양재역);
    }

    @DisplayName("지하철 노선의 가운데 구간을 제거")
    @Test
    void removeLineSectionInMiddle() {
        // given
        Long 정자역 = 지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(양재역, 정자역));

        // when
        지하철_노선에_지하철_구간_제거_요청(관리자, 신분당선, 양재역);

        // then
        노선에_역들이_순서대로_존재한다(신분당선, 강남역, 정자역);
    }

    private Map<String, String> createLineCreateParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "신분당선");
        lineCreateParams.put("color", "bg-red-600");
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", distance + "");
        return lineCreateParams;
    }

    private void 노선에_역들이_순서대로_존재한다(Long lineId, Long... stationIds) {
        var response = 지하철_노선_조회_요청(lineId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
    }

    private void 요청이_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
