package nextstep.subway.maps.map.acceptance;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.maps.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.maps.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.maps.line.acceptance.step.LineStationAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.maps.map.acceptance.step.PathAcceptanceStep.*;
import static nextstep.subway.maps.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static nextstep.subway.members.member.acceptance.step.MemberAcceptanceStep.로그인_되어_있음;
import static nextstep.subway.members.member.acceptance.step.MemberAcceptanceStep.회원_등록되어_있음;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final String EMAIl = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int EXPECTED_FARE = 1450;

    /**
     * 교대역      -      강남역
     * |                 |
     * 남부터미널역           |
     * |                 |
     * 양재역      -       -|
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("교대역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("양재역");
        ExtractableResponse<Response> createdStationResponse4 = 지하철역_등록되어_있음("남부터미널");
        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_등록되어_있음("2호선", "GREEN", 400);
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_등록되어_있음("신분당선", "RED", 0);
        ExtractableResponse<Response> createLineResponse3 = 지하철_노선_등록되어_있음("3호선", "ORANGE", 200);

        Long lineId1 = createLineResponse1.as(LineResponse.class).getId();
        Long lineId2 = createLineResponse2.as(LineResponse.class).getId();
        Long lineId3 = createLineResponse3.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        Long stationId4 = createdStationResponse4.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록되어_있음(lineId1, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId1, stationId2, 2, 2);

        지하철_노선에_지하철역_등록되어_있음(lineId2, null, stationId2, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId2, stationId3, 2, 1);

        지하철_노선에_지하철역_등록되어_있음(lineId3, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId1, stationId4, 1, 2);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId4, stationId3, 2, 2);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 출발역에서_도착역까지의_최단_혹은_최소시간_거리_경로_조회_요청("DISTANCE", 1L, 3L);

        //then
        적절한_경로를_응답(response, Lists.newArrayList(1L, 4L, 3L));
        총_거리와_소요_시간을_함께_응답함(response, 3, 4);
        지하철_이용_요금도_함께_응답함(response, EXPECTED_FARE);
    }


    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        //when
        ExtractableResponse<Response> response = 출발역에서_도착역까지의_최단_혹은_최소시간_거리_경로_조회_요청("DURATION", 1L, 3L);
        //then
        총_거리와_소요_시간을_함께_응답함(response, 4, 3);
        적절한_경로를_응답(response, Lists.newArrayList(1L, 2L, 3L));
        지하철_이용_요금도_함께_응답함(response, EXPECTED_FARE);
    }

    @DisplayName("청소년의 경우  운임에서 350원을 공제한 금액의 20% 할인을 받는다.")
    @Test
    void discountForYouth() {
        //given
        double discountAmount = (EXPECTED_FARE - 350) * 0.2;
        회원_등록되어_있음(EMAIl, PASSWORD, 15);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIl, PASSWORD);

        //when
        ExtractableResponse<Response> response = 출발역에서_도착역까지의_최단_혹은_최소시간_거리_경로_조회_요청("DURATION", tokenResponse, 1L, 3L);
        //then
        총_거리와_소요_시간을_함께_응답함(response, 4, 3);
        적절한_경로를_응답(response, Lists.newArrayList(1L, 2L, 3L));
        지하철_이용_요금도_함께_응답함(response, (int) (EXPECTED_FARE - discountAmount));
    }

    @DisplayName("어린이의 경우 운임에서 350원을 공제한 금액의 50% 할인을 받는다.")
    @Test
    void discountForChildren() {
        //given
        double discountAmount = (EXPECTED_FARE - 350) * 0.5;

        회원_등록되어_있음(EMAIl, PASSWORD, 10);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIl, PASSWORD);

        //when
        ExtractableResponse<Response> response = 출발역에서_도착역까지의_최단_혹은_최소시간_거리_경로_조회_요청("DURATION", tokenResponse, 1L, 3L);
        //then
        총_거리와_소요_시간을_함께_응답함(response, 4, 3);
        적절한_경로를_응답(response, Lists.newArrayList(1L, 2L, 3L));
        지하철_이용_요금도_함께_응답함(response, (int) (EXPECTED_FARE - discountAmount));
    }

    @DisplayName("성인의 경우 정상 요금을 받는다.")
    @Test
    void normalPriceForAdult() {
        //given
        회원_등록되어_있음(EMAIl, PASSWORD, 25);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIl, PASSWORD);

        //when
        ExtractableResponse<Response> response = 출발역에서_도착역까지의_최단_혹은_최소시간_거리_경로_조회_요청("DURATION", tokenResponse, 1L, 3L);
        //then
        총_거리와_소요_시간을_함께_응답함(response, 4, 3);
        적절한_경로를_응답(response, Lists.newArrayList(1L, 2L, 3L));
        지하철_이용_요금도_함께_응답함(response, EXPECTED_FARE);
    }

}
