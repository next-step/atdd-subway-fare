package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.AcceptanceTest;
import nextstep.subway.domain.WeightType;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 양재시민의숲역;

    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 15, 2);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 7, 5);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 구간_파라메터(남부터미널역, 양재역, 5, 4));
        지하철_노선에_지하철_구간_생성_요청(삼호선, 구간_파라메터(양재역, 양재시민의숲역, 59, 8));
    }

    @DisplayName("두 역의 거리 경로를 조회한다.")
    @Test
    void 경로_조회() {
        // when
        ExtractableResponse<Response> 최단_거리_경로_조회 = 두_역의_경로_조회를_요청(교대역, 양재역, WeightType.DISTANCE);

        // then
        지하철_경로_역_확인(최단_거리_경로_조회, 교대역, 남부터미널역, 양재역);
        경로_거리_확인(최단_거리_경로_조회, 12);

        // when
        ExtractableResponse<Response> 최단_시간_경로_조회 = 두_역의_경로_조회를_요청(교대역, 양재역, WeightType.DURATION);

        // then
        지하철_경로_역_확인(최단_시간_경로_조회, 교대역, 강남역, 양재역);
        소요_시간_확인(최단_시간_경로_조회, 4);
    }

    @DisplayName("거리에 따른 운임비용")
    @Test
    void 거리에_따른_요금() {
        // when
        ExtractableResponse<Response> 거리_10km_이내_요금 = 두_역의_경로_조회를_요청(교대역, 남부터미널역, WeightType.DISTANCE);

        // then
        지하철_경로_역_확인(거리_10km_이내_요금, 교대역, 남부터미널역);
        요금_확인(거리_10km_이내_요금, 1250);

        // when
        ExtractableResponse<Response> 거리_10km_50km_이내_요금 = 두_역의_경로_조회를_요청(교대역, 양재역, WeightType.DISTANCE);

        // then
        지하철_경로_역_확인(거리_10km_50km_이내_요금, 교대역, 남부터미널역, 양재역);
        요금_확인(거리_10km_50km_이내_요금, 1350);

        // when
        ExtractableResponse<Response> 거리_50km_이상_요금 = 두_역의_경로_조회를_요청(양재역, 양재시민의숲역, WeightType.DISTANCE);

        // then
        지하철_경로_역_확인(거리_50km_이상_요금, 양재역, 양재시민의숲역);
        요금_확인(거리_50km_이상_요금, 2250);
    }


}
