package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회_검증;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_검증;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.정상_요청이_아닐_경우_예외_처리한다;
import static nextstep.subway.acceptance.PathSteps.추가_요금이_있는_노선을_이용_할_경우_측정된_요금에_추가한다;
import static nextstep.subway.acceptance.PathSteps.추가요금이_있는_노선을_환승_하여_이용_할_경우_가장_높은_금액의_추가_요금만_적용한다;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 정자역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 팔호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *8호선* ---   양재       정자역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10, 900);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10, 400);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 2, 200);
        팔호선 = 지하철_노선_생성_요청("8호선", "yellow", 남부터미널역, 양재역, 3, 10, 0);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        두_역의_경로_조회_검증(response, 교대역, 남부터미널역, 양재역);
    }

    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금을 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByTime() {
        // when
        var response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        두_역의_경로_조회_검증(response, 교대역, 남부터미널역, 양재역);

        // and
        두_역의_최소_시간_경로_조회를_검증(response, 5L, 12L, 1450);
    }

    /**
     * when 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청
     * and 추가 요금이 있는 노선을 이용 할 경우
     * then 측정된 요금에 추가한다
     */
    @DisplayName("추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가한다.")
    @Test
    void findPathByDistanceAdditionalFare() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 남부터미널역);

        // then
        추가_요금이_있는_노선을_이용_할_경우_측정된_요금에_추가한다(response, 1450);
    }

    /**
     * when 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청
     * and 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우
     * then 가장 높은 금액의 추가 요금만 적용한다
     */
    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.")
    @Test
    void findPathByDistanceAdditionalFareOnlyHighestAmount() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(남부터미널역, 강남역);

        // then
        추가요금이_있는_노선을_환승_하여_이용_할_경우_가장_높은_금액의_추가_요금만_적용한다(response, 2250);
    }

    /**
     * when 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청
     * then 로그인 사용자의 경우 연령별 요금으로 계산한다
     * and 청소년: 운임에서 350원을 공제한 금액의 20%할인
     * and 어린이: 운임에서 350원을 공제한 금액의 50%할인
     */
    @DisplayName("로그인 사용자의 경우 연령별 요금으로 계산")
    @Test
    void findPathByDistanceFareCalculateByLoginUserAge() {
        // 청소년
        사용자_연령별_요금_계산(청소년_EMAIL, 청소년_PASSWORD, 1870);

        // 어린이
        사용자_연령별_요금_계산(어린이_EMAIL, 어린이_PASSWORD, 1300);

        // 성인
        사용자_연령별_요금_계산(EMAIL, PASSWORD, 2250);
    }

    private void 사용자_연령별_요금_계산(final String email, final String password, final int fare) {
        // given
        String accessToken = 베어러_인증_로그인_요청(email, password).jsonPath().getString("accessToken");
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(남부터미널역, 강남역, accessToken);
        // then
        추가요금이_있는_노선을_환승_하여_이용_할_경우_가장_높은_금액의_추가_요금만_적용한다(response, fare);
    }

    @DisplayName("경로 조회 예외 처리 기능")
    @Nested
    class PathExceptionTest {
        /**
         * When 연결되지 않은 역의 최단 거리 기준으로 경로 조회를 요청시
         * Then 예외 처리한다
         * When 연결되지 않은 역의 최소 시간 기준으로 경로 조회를 요청시
         * Then 예외 처리한다
         */
        @DisplayName("연결되지 않은 역으로 경로 조회를 요청시 예외 처리한다.")
        @Test
        void findPathNotConnectedStation() {
            var findByDistanceResponse = 두_역의_최단_거리_경로_조회를_요청(교대역, 정자역);

            정상_요청이_아닐_경우_예외_처리한다(findByDistanceResponse);

            var findByDurationResponse = 두_역의_최소_시간_경로_조회를_요청(교대역, 정자역);

            정상_요청이_아닐_경우_예외_처리한다(findByDurationResponse);
        }

        /**
         * When 존재하지 않는 역의 최단 거리 기준으로 경로 조회를 요청시
         * Then 예외 처리한다
         * When 존재하지 않는 역의 최소 시간 기준으로 경로 조회를 요청시
         * Then 예외 처리한다
         */
        @DisplayName("존재하지 않는 역으로 경로 조회를 요청시 예외 처리한다.")
        @Test
        void findPathNotExistStation() {
            var findByDistanceResponse = 두_역의_최단_거리_경로_조회를_요청(교대역, Long.MAX_VALUE);

            정상_요청이_아닐_경우_예외_처리한다(findByDistanceResponse);

            var findByDurationResponse = 두_역의_최소_시간_경로_조회를_요청(교대역, Long.MAX_VALUE);

            정상_요청이_아닐_경우_예외_처리한다(findByDurationResponse);
        }
    }
}
