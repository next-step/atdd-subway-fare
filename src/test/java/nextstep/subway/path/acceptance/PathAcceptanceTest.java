package nextstep.subway.path.acceptance;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2, 10);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3, 10);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회")
    @Test
    void findPaths() {

        // when
        ExtractableResponse<Response> distanceResponse = 두_역의_최단거리_또는_최소시간_경로_조회_요청(교대역.getId(), 양재역.getId(),"DISTANCE");

        // then
        경로_응답됨(distanceResponse, Lists.newArrayList(교대역.getId(), 남부터미널역.getId(), 양재역.getId()));
        총_거리와_소요_시간을_함께_응답함(distanceResponse,5, 20);
        이용요금도_함께_응답함(distanceResponse, 1250);

        // when
        ExtractableResponse<Response> durationResponse = 두_역의_최단거리_또는_최소시간_경로_조회_요청(교대역.getId(), 양재역.getId(), "DURATION");

        // then
        경로_응답됨(durationResponse, Lists.newArrayList(교대역.getId(), 강남역.getId(), 양재역.getId()));
        총_거리와_소요_시간을_함께_응답함(durationResponse, 20, 20);
        이용요금도_함께_응답함(durationResponse, 1450);
    }
}
