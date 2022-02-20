package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.createLineCreateParams;
import static nextstep.subway.acceptance.LineSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청_하고_ID_반환;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathStep.가장_빠른_도착_경로_조회_성공;
import static nextstep.subway.acceptance.PathStep.가장_빠른_도착_경로_조회를_요청;
import static nextstep.subway.acceptance.PathStep.경로_조회_성공;
import static nextstep.subway.acceptance.PathStep.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathStep.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역, 강남역, 강남역_다음_역, 양재역, 남부터미널역;
    private Long 이호선, 신분당선, 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *
     * 교대역 -> 강남역 = 거리 6, 소요 시간 2, 배차 시간 10분
     * 강남역 -> 양재역 = 거리 10, 소요 시간 3, 배차 시간 20분
     *
     * 교대역 -> 남부터미널역 = 거리 2, 소요 시간 2, 배차 시간 30분
     * 남부터미널역 -> 양재역 = 거리 10, 소요 시간 4, 배치 시간 30분
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        강남역_다음_역 = 지하철역_생성_요청("강남역_다음_역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청_하고_ID_반환(
            createLineCreateParams("2호선", "green", 교대역, 강남역, 6, 2, 0,
                                   LocalTime.of(5, 0),
                                   LocalTime.of(23, 0),
                                   LocalTime.of(0, 10)
            )
        );
        신분당선 = 지하철_노선_생성_요청_하고_ID_반환(
            createLineCreateParams("신분당선", "red", 강남역, 양재역, 10, 3, 0,
                                   LocalTime.of(5, 0),
                                   LocalTime.of(23, 0),
                                   LocalTime.of(0, 20)
            )
        );
        삼호선 = 지하철_노선_생성_요청_하고_ID_반환(
            createLineCreateParams("3호선", "orange", 교대역, 남부터미널역, 2, 2, 0,
                                   LocalTime.of(5, 0),
                                   LocalTime.of(23, 0),
                                   LocalTime.of(0, 30)
            )
        );

        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(강남역, 강남역_다음_역, 1, 8));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 10, 4));
    }


    /**
     *
     * Feature: 지하철 경로 탐색
     *
     *  Scenario: 기준을 변경해가며 경로를 탐색 한다.
     *  Given   지하철 노선이 등록 되어 있고
     *  And     지하철 노선에 지하철 역이 등록 되어있다.
     *
     *  When    두 역의 최단 거리 경로를 조회한다.
     *  Then    두 역의 최단 거리 검색 결과가 조회된다.
     *          [교대역-양재역 =
     *          거리 : 12 / 소요 시간 : 6 / 요금 : 1,350원 / 경로 : 교대역, 남부터미널역, 양재역]
     *
     *  When    두 역의 최소 시간 경로를 조회한다.
     *  Then    두 역의 최소 시간 검색 결과가 조회된다.
     *          [교대역-양재역 =
     *          거리 : 16 / 소요 시간 : 5 / 요금 : 1,450원 / 경로 : 교대역, 강남역, 양재역]
     *
     *  When    한 종류의 노선에서 도착 시간을 기준으로 가장 빠른 도착 경로를 조회한다.
     *          [교대역-강남역 다음 역, 10시 0분 출발 =
     *          도착 : 10시 10분 / 거리 : 7 / 소요 시간 : 10 / 요금 : 1,250원 / 경로 : 교대역, 강남역, 강남역 다음 역]
     *
     *  When    여러 노선에 걸쳐 가장 빠른 도착 경로를 조회한다. - 다른 노선의 첫 역으로 환승 할 때
     *  Then    [교대역-양재역, 10시 0분 출발 =
     *          도착 : 10시 23분 / 거리 : 16 / 소요 시간 : 5 / 요금 : 1,450원 / 경로 : 교대역, 강남역, 양재역]
     *
     *  Given   신분당선에 강남역 이전 역이 등록 되어있다.
     *  When    여러 노선에 걸쳐 가장 빠른 도착 경로를 조회한다. - 다른 노선의 첫 역이 아닌 역으로 환승 할 때
     *  Then    [교대역-양재역, 10시 0분 출발 =
     *          도착 : 10시 24분 / 거리 : 16 / 소요 시간 : 5 / 요금 : 1,450원 / 경로 : 교대역, 강남역, 양재역]
     *
     * */

    @DisplayName("지하철 경로 탐색 인수 테스트 통합")
    @Test
    void findPath() {
        // 두 역의 최단 거리 경로를 조회한다.
        경로_조회_성공(
            두_역의_최단_거리_경로_조회를_요청(교대역, 양재역),
            12, 6, 1350,
            교대역, 남부터미널역, 양재역
        );

        // 두 역의 최소 시간 경로를 조회한다.
        경로_조회_성공(
            두_역의_최소_시간_경로_조회를_요청(교대역, 양재역),
            16, 5,1450,
            교대역, 강남역, 양재역
        );

        // 한 종류의 노선에서 도착 시간을 기준으로 가장 빠른 도착 경로를 조회한다.
        LocalDateTime 출발_시각 = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        LocalDateTime 가장_빠른_도착_일시 = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 10));
        가장_빠른_도착_경로_조회_성공(
            가장_빠른_도착_경로_조회를_요청(RestAssured.given().log().all(), 교대역, 강남역_다음_역, 출발_시각),
            7, 10, 1250, 가장_빠른_도착_일시,
            교대역, 강남역, 강남역_다음_역
        );

        // 여러 노선에 걸쳐 가장 빠른 도착 경로를 조회한다. - 다른 노선의 첫 역으로 환승 할 때
        출발_시각 = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        가장_빠른_도착_일시 = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 23));
        가장_빠른_도착_경로_조회_성공(
            가장_빠른_도착_경로_조회를_요청(RestAssured.given().log().all(), 교대역, 양재역, 출발_시각),
            16, 5, 1450, 가장_빠른_도착_일시,
            교대역, 강남역, 양재역
        );

        // Given 신분당선에 강남역 이전 역이 등록 되어있다.
        Long 강남역_이전_역 = 지하철역_생성_요청("강남역_이전_역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(강남역_이전_역, 강남역, 1, 1));

        // 여러 노선에 걸쳐 가장 빠른 도착 경로를 조회한다. - 다른 노선의 첫 역이 아닌 역으로 환승 할 때
        출발_시각 = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        가장_빠른_도착_일시 = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 24));
        가장_빠른_도착_경로_조회_성공(
            가장_빠른_도착_경로_조회를_요청(RestAssured.given().log().all(), 교대역, 양재역, 출발_시각),
            16, 5, 1450, 가장_빠른_도착_일시,
            교대역, 강남역, 양재역
        );
    }
}
