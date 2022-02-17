package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.common.AcceptanceTest;
import nextstep.subway.path.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.path.acceptance.PathUtils.*;
import static nextstep.subway.station.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**          (40km, 40min)
     * 교대역    --- *2호선* ---   강남역
     *   |                          |
     * (33km, 33min)             (40km, 40min)
     * *3호선*                   *신분당선*
     *   |                          |
     * 남부터미널역  --- *3호선* --- 양재
     *               (33km, 33min)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 40, 40);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 40, 40);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 33, 33);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 33, 33));
    }

    @ParameterizedTest(name = "두 역의 경로를 조회한다. [{arguments}]")
    @EnumSource(PathType.class)
    void findPath(PathType type) {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, type);

        // then
        경로_조회_성공(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }
}
