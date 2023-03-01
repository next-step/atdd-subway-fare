package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.PathSteps.노선에_추가_요금_추가_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.로그인하고_두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.utils.PathAssertionUtils.최적경로의_거리는_다음과_같다;
import static nextstep.subway.utils.PathAssertionUtils.최적경로의_소요시간은_다음과_같다;
import static nextstep.subway.utils.PathAssertionUtils.최적경로의_역_아이디_목록의_순서는_다음과_같다;
import static nextstep.subway.utils.PathAssertionUtils.최적경로의_요금은_다음과_같다;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private static final String CHILD_EMAIL = "child6@email.com";
    private static final String TEENAGER_EMAIL = "teenager13@email.com";
    private static final String ADULT_EMAIL = "adult19@email.com";
    private static final String PASSWORD = "password";

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    private String 어린이;
    private String 청소년;
    private String 어른;

    /**           10km, 5min
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*(2km, 100min)   *신분당선*(10km, 10min)
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *              3km, 100min
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 5);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 100);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 100));
    }

    /**
     * when : 두 역의 최단 거리 경로 조회를 요청하면
     * then : 최단 거리 경로의 역 목록을 조회할 수 있다.
     * then : 총 거리와 소요 시간을 조회할 수 있다.
     * then : 지하철 이용 요금을 조회할 수 있다.
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        최적경로의_역_아이디_목록의_순서는_다음과_같다(response, 교대역, 남부터미널역, 양재역);
        최적경로의_거리는_다음과_같다(response, 5);
        최적경로의_소요시간은_다음과_같다(response, 200);
        최적경로의_요금은_다음과_같다(response, 1250);
    }

    /**
     * when : 두 역의 최단 시간 경로 조회를 요청하면
     * then : 최소 시간 경로의 역 목록을 조회할 수 있다.
     * then : 총 거리와 소요 시간을 조회할 수 있다.
     * then : 지하철 이용 요금을 조회할 수 있다.
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        최적경로의_역_아이디_목록의_순서는_다음과_같다(response, 교대역, 강남역, 양재역);
        최적경로의_거리는_다음과_같다(response, 20);
        최적경로의_소요시간은_다음과_같다(response, 15);
        최적경로의_요금은_다음과_같다(response, 1450);
    }

    /**
     * given: 노선에 추가 요금 정책을 추가하고
     * when : 최단 거리 경로 요청을 하면
     * then : 거리 비례 요금과 노선별 추가 요금이 합산되어 요금이 책정된다.
     */
    @DisplayName("노선에 추가 요금이 있는 경우 거리 비례 요금과 노선별 추가 요금 중 가장 높은 추가 요금이 합산되어 요금이 책정된다")
    @Test
    void findPathWhenHasExtraFareLine() {
        // given
        int 삼호선_추가요금 = 900;
        int 이호선_추가요금 = 1200;
        노선에_추가_요금_추가_요청(삼호선, 삼호선_추가요금);
        노선에_추가_요금_추가_요청(이호선, 이호선_추가요금);

        // when
        final ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(남부터미널역, 강남역);

        // then
        최적경로의_거리는_다음과_같다(response, 12);
        최적경로의_요금은_다음과_같다(response, 1350 + 이호선_추가요금);
    }

    /**
     * given: 어린이(6세)으로 로그인한 사용자가
     * when : 최단 거리 경로 요청을 하면
     * then : 거리 비례 요금과 어린이 할인 요금이 적용된 요금이 책정된다.
     */
    @DisplayName("어린이(6세)으로 로그인한 사용자가 최단 거리 경로 요청을 하면 어린이 할인 정책이 적용된 거리 비례 요금이 책정된다.")
    @Test
    void findPathWhen6AgeMember() {
        // given
        어린이 = 베어러_인증_로그인_요청(CHILD_EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        final ExtractableResponse<Response> response = 로그인하고_두_역의_최단_거리_경로_조회를_요청(어린이, 교대역, 강남역);

        // then
        최적경로의_거리는_다음과_같다(response, 10);
        최적경로의_요금은_다음과_같다(response, (int)((1250 - 350) * 0.5));
    }

    /**
     * given: 청소년(13세)으로 로그인한 사용자가
     * when : 최단 거리 경로 요청을 하면
     * then : 거리 비례 요금과 청소년 할인 요금이 적용된 요금이 책정된다.
     */
    @DisplayName("청소년(13세)으로 로그인한 사용자가 최단 거리 경로 요청을 하면 청소년 할인 정책이 적용된 거리 비례 요금이 책정된다.")
    @Test
    void findPathWhen13AgeMember() {
        // given
        청소년 = 베어러_인증_로그인_요청(TEENAGER_EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        final ExtractableResponse<Response> response = 로그인하고_두_역의_최단_거리_경로_조회를_요청(청소년, 교대역, 강남역);

        // then
        최적경로의_거리는_다음과_같다(response, 10);
        최적경로의_요금은_다음과_같다(response, (int)((1250 - 350) * 0.8));
    }

    /**
     * given: 어른(19세)으로 로그인한 사용자가
     * when : 최단 거리 경로 요청을 하면
     * then : 연령별 할인 혜택이 없으므로 거리 비례 요금만 책정된다.
     */
    @DisplayName("어른(19세)으로 로그인한 사용자가 최단 거리 경로 요청을 하면 연령별 할인 정책이 적용되지 않는다.")
    @Test
    void findPathWhen19AgeMember() {
        // given
        어른 = 베어러_인증_로그인_요청(ADULT_EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        final ExtractableResponse<Response> response = 로그인하고_두_역의_최단_거리_경로_조회를_요청(어른, 교대역, 강남역);

        // then
        최적경로의_거리는_다음과_같다(response, 10);
        최적경로의_요금은_다음과_같다(response, 1250);
    }

    /**
     * given: 로그인한 사용자가 추가 요금이 있는 노선의
     * when : 최단 거리 경로를 요청하면
     * then : "(거리 비례 요금 + 노선 추가 요금) * 연령별 할인 정책"의 계산식으로 계산된 최종 요금을 조회할 수 있다.
     */
    @DisplayName("청소년이 추가 요금이 있는 노선의 최단 거리 경로 요청을 하면 가격 정책이 반영된 요금을 조회할 수 있다.")
    @Test
    void findPathWhen13AgeMemberAndExtraFareLine() {
        // given
        int 이호선_추가요금 = 1200;
        노선에_추가_요금_추가_요청(이호선, 이호선_추가요금);
        청소년 = 베어러_인증_로그인_요청(TEENAGER_EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        final ExtractableResponse<Response> response = 로그인하고_두_역의_최단_거리_경로_조회를_요청(청소년, 교대역, 강남역);

        // then
        최적경로의_거리는_다음과_같다(response, 10);
        최적경로의_요금은_다음과_같다(response, (int)((1250 + 1200 - 350) * 0.8));
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
