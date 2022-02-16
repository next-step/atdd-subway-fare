package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.common.AcceptanceTest;
import nextstep.subway.line.acceptance.LineSteps;
import nextstep.subway.path.domain.PathType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

    /**          (10km, 10min)
     * 교대역    --- *2호선* ---   강남역
     *   |                          |
     * (2km, 2min)             (10km, 10min)
     * *3호선*                   *신분당선*
     *   |                          |
     * 남부터미널역  --- *3호선* --- 양재
     *               (3km, 3min)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
    }

    /*
        그동안 집착을 해온 것이 어떻게든 인수테스트를 통과시키고 다음으로 넘어가야 한다는 것이었습니다.
        그런데 자료를 찾아보니 인수테스트 코드만 작성을하고 다음단계(TDD)로 넘어간뒤 모든 과정이 끝나면
        인수테스트가 통과하는지 본다 라는 자료가 있어서요.
        저도 이 내용에 동의하는데 리뷰어님은 인수테스트코드가 통과된 뒤 다음으로 넘어가시는지 의견을 묻고 싶습니다 :)
     */
    @ParameterizedTest(name = "두 역의 경로를 조회한다. [{arguments}]")
    @MethodSource("providePathType")
    void findPath(PathType type) {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, type);

        // then
        assertAll(() -> {
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
            assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
            assertThat(response.jsonPath().getInt("duration")).isEqualTo(5);
        });
        /*
            어느 리뷰어님의 리뷰를 보니 하나의 테스트에는 하나의 검증이 있는 것을 추천한다면서
            여러 검증이 필요한 경우 assertAll을 사용하라고 추천했습니다.
            제가 생각하기에는 assertAll을 써도 검증하는 것이 3개인데 차이점이 궁금합니다 :)
         */
    }

    private static Stream<Arguments> providePathType() {
        return Stream.of(
                Arguments.of(PathType.DISTANCE),
                Arguments.of(PathType.DURATION)
        );
    }
}
