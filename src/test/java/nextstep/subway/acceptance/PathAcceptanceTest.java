package nextstep.subway.acceptance;

import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.로그인_사용자가_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.PathSteps.추가_요금이_있는_지하철_노선_생성_요청;
import static nextstep.subway.acceptance.SectionSteps.구간_생성_요청값_생성;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 선릉역;
    private Long 수원역;
    private Long 이호선;
    private Long 신분당선;
    private Long 분당선;
    private Long 삼호선;

    /** [거리/시간]
     * - 2호선 + 500
     * - 분당선 + 1000
     * <p>
     *               [10/2]                      [10/10]
     * 교대역    --- *2호선*   ---    강남역    ---------   선릉
     * |                                |                    |
     * *3호선* [2/2]               *신분당선*  [10/1]      *분당선* [20/20] + 1000
     * |                                |                    |
     * 남부터미널역  --- *3호선* ---   양재                 수원
     *                   [10/2]
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        선릉역 = 지하철역_생성_요청("선릉역").jsonPath().getLong("id");
        수원역 = 지하철역_생성_요청("수원역").jsonPath().getLong("id");

        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 1);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 2);
        이호선 = 추가_요금이_있는_지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2, 500);
        분당선 = 추가_요금이_있는_지하철_노선_생성_요청("분당선", "yellow", 선릉역, 수원역, 20, 20, 1000);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 구간_생성_요청값_생성(남부터미널역, 양재역, 10, 2));
        지하철_노선에_지하철_구간_생성_요청(이호선, 구간_생성_요청값_생성(강남역, 선릉역, 10, 10));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    // Given 어린이, 청소년, 일반 사용자가 로그인을 하고
    // When 경로를 조회하면
    // Then 할인이 적용된 금액을 확인할 수 있다.
    @DisplayName("로그인 사용자가 두 역의 최단 거리 경로를 조회한다.")
    @MethodSource
    @ParameterizedTest(name = "{0} 사용자의 할인된 금액은 {1}원")
    void findPathWithLogin(GithubResponses githubResponse, int expected) {
        String accessToken = 깃허브_인증_로그인_요청(githubResponse.getCode())
                .jsonPath()
                .getString("accessToken");

        var response = 로그인_사용자가_경로_조회를_요청(accessToken, 교대역, 양재역);

        assertThat(response.jsonPath().getInt("cost")).isEqualTo(expected);
    }

    static Stream<Arguments> findPathWithLogin() {
        return Stream.of(
                arguments(GithubResponses.어린이_사용자, 850),
                arguments(GithubResponses.청소년_사용자, 1150),
                arguments(GithubResponses.사용자1, 1350)
        );
    }

    @DisplayName("출발/도착역이 동일한 경우 경로를 조회할 수 없다.")
    @Test
    void findCircularPath() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 교대역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("존재하지 않는 역이 포함되어 있는 경우 조회할 수 없다.")
    @Test
    void findPathWithInvalidStation() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, Long.MAX_VALUE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 기준 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        var response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
    }

    @DisplayName("경로 조회 시 부과된 요금을 확인할 수 있다.")
    @Test
    void findPathWithFare() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getInt("cost")).isEqualTo(1_350);
    }

    @DisplayName("경로 조회 시 해당 노선에 추가 요금이 있는 경우 요금에 추가된다.")
    @Test
    void findPathWithAdditionalFare() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 수원역);

        // 교대역 -> 강남역 -> 선릉역 -> 수원역 = 40Km + 가장 높은 금액의 추가 요금
        assertThat(response.jsonPath().getInt("cost")).isEqualTo(1_850 + 1_000);
    }
}
