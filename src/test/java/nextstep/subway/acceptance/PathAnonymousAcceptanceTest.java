package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.PathSteps.경로조회_비회원;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("비회원 경로 조회")
public class PathAnonymousAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 3, 900);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
    }


    @DisplayName("(비회원) 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceForAnonymous() {
        //given
        Map<String, String> 경로_조회_파라미터 = 경로_조회_파라미터_생성(교대역, 양재역, "SHORTEST_DISTANCE");

        // when
        ExtractableResponse<Response> response = 경로조회_비회원(경로_조회_파라미터);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(6),
                () -> assertThat(response.jsonPath().getInt("fee")).isEqualTo(2150)
        );
    }


    /**
     * 저도 해당 내용이 이상한게 해당 테스트 '만' 실행하면 성공하고 같이 실행 하면 실패하는 모습이 보이는데 테스트 간에 영향을 주고 있는 것 같은데... 이유가 확인이 안되는 것 같습니다.
     */
    @DisplayName("(비회원) 두 역의 최소 시간 경로를 조회")
    @Test
    void findPathByDurationForAnonymous() {
        //given
        Map<String, String> 경로_조회_파라미터 = 경로_조회_파라미터_생성(교대역, 양재역, "MINIMUM_TIME");

        //when
        ExtractableResponse<Response> response = 경로조회_비회원(경로_조회_파라미터);

        //then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(20),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("fee")).isEqualTo(1450)
        );
    }
}
