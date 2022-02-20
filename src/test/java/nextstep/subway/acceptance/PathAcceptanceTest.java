package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역, 강남역, 남부터미널역, 양재역, 양재시민의숲역, 미금역, 이호선, 신분당선, 삼호선;

    /**
     *   [교대역] ------ 2호선(10km) ------ [강남역]
     *     |                               |
     *  3호선(2km)                     신분당선(10km)
     *     |                               |
     * [남부터미널역] ---- 3호선(3km) ------ [양재역]
     *                                     |
     *                                신분당선(1km)
     *                                     |
     *                                [양재시민의숲역]
     *                                     |
     *                                신분당선(46km)
     *                                     |
     *                                  [미금역]
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 3);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 1);

        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 양재시민의숲역, 1, 1));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 2));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(createPathParams(교대역, 양재역, PathType.DISTANCE));

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(createPathParams(교대역, 양재역, PathType.DURATION));

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(3);
    }

    @DisplayName("지하철 이용 요금을 조회한다.")
    @Test
    @Disabled
    void findFare() {
        // 기본 운임 (10㎞ 이내) : 1,250원
        // 교대역 -> 양재역 (5km)
        ExtractableResponse<Response> 교대역_양재역_경로_조회_응답 = 두_역의_경로_조회를_요청(createPathParams(교대역, 양재역, PathType.DISTANCE));
        assertThat(교대역_양재역_경로_조회_응답.jsonPath().getInt("fare")).isEqualTo(1250);

        // 이용 거리 초과 시 추가 운임 (10km ~ 50km) : 1~5km마다 100원
        // 강남역 -> 양재시민의숲역 (11km)
        ExtractableResponse<Response> 강남역_양재시민의숲역_경로_조회_응답 = 두_역의_경로_조회를_요청(createPathParams(강남역, 양재시민의숲역, PathType.DISTANCE));
        assertThat(강남역_양재시민의숲역_경로_조회_응답.jsonPath().getInt("fare")).isEqualTo(1350);

        // 이용 거리 초과 시 추가 운임 (50km) : 1~8km마다 100원
        // 강남역 -> 미금역 (57km)
        ExtractableResponse<Response> 강남역_미금역_경로_조회_응답 = 두_역의_경로_조회를_요청(createPathParams(강남역, 미금역, PathType.DISTANCE));
        assertThat(강남역_미금역_경로_조회_응답.jsonPath().getInt("fare")).isEqualTo(1350);
    }

}
