package nextstep.subway.maps.map.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.maps.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.maps.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.maps.line.acceptance.step.LineStationAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.maps.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 경로와 요금 검색")
public class PathFareAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("교대역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("양재역");
        ExtractableResponse<Response> createdStationResponse4 = 지하철역_등록되어_있음("남부터미널");
        ExtractableResponse<Response> createdStationResponse5 = 지하철역_등록되어_있음("양재시민의숲");
        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_등록되어_있음("신분당선", "RED");
        ExtractableResponse<Response> createLineResponse3 = 지하철_노선_등록되어_있음("3호선", "ORANGE");

        Long 이호선 = createLineResponse1.as(LineResponse.class).getId();
        Long 신분당선 = createLineResponse2.as(LineResponse.class).getId();
        Long 삼호선 = createLineResponse3.as(LineResponse.class).getId();
        Long 교대역 = createdStationResponse1.as(StationResponse.class).getId();
        Long 강남역 = createdStationResponse2.as(StationResponse.class).getId();
        Long 양재역 = createdStationResponse3.as(StationResponse.class).getId();
        Long 남부터미널 = createdStationResponse4.as(StationResponse.class).getId();
        Long 양재시민의숲 = createdStationResponse5.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록되어_있음(이호선, null, 교대역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 2, 2);

        지하철_노선에_지하철역_등록되어_있음(신분당선, null, 강남역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 2, 1);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 양재시민의숲, 2, 1);

        지하철_노선에_지하철역_등록되어_있음(삼호선, null, 교대역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널, 1, 2);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널, 양재역, 2, 2);
    }

    @DisplayName("")
    @Test
    void test() {

    }
}
