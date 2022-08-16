package nextstep.acceptance.test;

import nextstep.path.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.acceptance.steps.LineSectionSteps.given;
import static nextstep.acceptance.steps.LineSectionSteps.*;
import static nextstep.acceptance.steps.MemberSteps.*;
import static nextstep.acceptance.steps.PathSteps.경로_조회_정보가_일치한다;
import static nextstep.acceptance.steps.PathSteps.경로를_조회한다;
import static nextstep.acceptance.steps.StationSteps.지하철역_생성_요청;

class PathAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     *
     * 1. 거리 기준
     *
     * 교대역    --- *2호선(6)* ---     강남역
     * |                              |
     * *3호선(10)*                   *신분당선(10)*
     * |                              |
     * 남부터미널역  --- *3호선(3)* ---   양재역
     *
     * 2. 소요시간 기준
     *
     * 교대역    --- *2호선(8)* ---     강남역
     * |                              |
     * *3호선(3)*                   *신분당선(5)*
     * |                              |
     * 남부터미널역  --- *3호선(7)* ---   양재역
     *
     *
     * 주의: <<2호선인 교대역을 지날경우 추가 요금 900원 추가>>
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청(관리자, createLineCreateParams("2호선", "green", 900, 교대역, 강남역, 6, 8))
                .jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청(관리자, createLineCreateParams("신분당선", "red", 강남역, 양재역, 10, 5))
                .jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청(관리자, createLineCreateParams("3호선", "orange", 교대역, 남부터미널역, 10, 3))
                .jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 7));
    }

    @DisplayName("최단 거리 경로를 조회한다.")
    @Test
    void pathByDistance() {
        // when
        var response = 경로를_조회한다(남부터미널역, 강남역, PathType.DISTANCE, given());

        // then
        경로_조회_정보가_일치한다(response, 13, 12, 1350, 남부터미널역, 양재역, 강남역);
    }

    @DisplayName("최소 시간 경로를 조회한다.")
    @Test
    void pathByDuration() {
        // when
        var response = 경로를_조회한다(남부터미널역, 강남역, PathType.DURATION, given());

        // then
        경로_조회_정보가_일치한다(response, 16, 11, 2250, 남부터미널역, 교대역, 강남역);
    }

    @DisplayName("로그인 사용자는 연령에 따라 할인을 받는다. (6세 ~ 12세: 50%, 13세 ~ 18세: 20%)")
    @Test
    void pathByDurationAndDiscount() {
        // given
        회원_생성_요청(createMemberParams("teenager@email.com", "password", 15));
        String 청소년 = 로그인_되어_있음("teenager@email.com", "password");

        // when
        var response = 경로를_조회한다(남부터미널역, 강남역, PathType.DURATION, given(청소년));

        // then
        경로_조회_정보가_일치한다(response, 16, 11, 1800, 남부터미널역, 교대역, 강남역);
    }
}
