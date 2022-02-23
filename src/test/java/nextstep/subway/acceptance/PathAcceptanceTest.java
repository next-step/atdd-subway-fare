package nextstep.subway.acceptance;

import lombok.val;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 연신내역;
    private Long 서울역;

    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long GTX;

    /**
     *                         (10km, 2분)
     *              교대역    --- *2호선* ---    강남역
     *               |                           |
     * (2km, 9분)  *3호선*                     *신분당선* (10km, 3분)
     *               |                           |
     *             남부터미널역 --- *3호선* ---    양재
     *                         (3km, 8분)
     *
     *
     *             연신내역 --- *GTX* ---    서울역
     *                         (50km, 39분)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        연신내역 = 지하철역_생성_요청("연신내역").jsonPath().getLong("id");
        서울역 = 지하철역_생성_요청("서울역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 9);
        GTX = 지하철_노선_생성_요청("GTX", "pupple", 연신내역, 서울역, 58, 30);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 8));
    }

    /**
     * @see nextstep.subway.ui.PathController#findPath
     */
    @DisplayName("두_역의_최단_거리_경로_조회")
    @Test
    void findPathByDistance() {
        // when
        val response = 두_역의_경로_조회(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(17);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    /**
     * @see nextstep.subway.ui.PathController#findPath
     */
    @DisplayName("두_역의_최단_시간_경로_조회")
    @Test
    void findPathByDuration() {
        // when
        val response = 두_역의_경로_조회(교대역, 양재역, PathType.DURATION);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(20);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1450);
    }

    /**
     * 1250(1~10) + 800 (11 ~ 50) + 200 (51 ~ 58)
     * @see nextstep.subway.ui.PathController#findPath
     */
    @DisplayName("두_역의_최단_시간_경로_조회_50KM이상")
    @Test
    void 두_역의_최단_시간_경로_조회_50KM이상() {
        // when
        val response = 두_역의_경로_조회(연신내역, 서울역, PathType.DURATION);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(연신내역, 서울역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(58);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(30);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
    }
}
