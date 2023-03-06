package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.SearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * 지하철 운임은 거리비례제로 책정됩니다. (실제 경로가 아닌 최단거리 기준)
 * <p>
 * 기본운임(10㎞ 이내) : 기본운임 1,250원
 * 이용 거리초과 시 추가운임 부과
 * 10km초과∼50km까지(5km마다 100원)
 * 50km초과 시 (8km마다 100원)
 */
@DisplayName("요금 계산 인수 테스트")
public class FareAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 역삼역;
    private Long 선릉역;
    private Long 왕십리역;
    private Long 신촌역;
    private Long 종합운동장역;
    private Long 건대입구역;
    private Long 사당역;

    private Long 이호선;

    /**
     * <이동거리|이동시간>
     * 교대역 --- <10|10> --- 강남역 --- <2|2> --- 역삼역 --- <3|3> --- 선릉역  --- <1|1> --- 왕십리역 *하행종착역*
     * |
     * <50|50>
     * |
     * 신촌역 --- <8|8> --- 종합운동작역 --- <4|4> --- 건대입구역 --- <4|4> --- 사당역 *상행종착역*
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청("역삼역").jsonPath().getLong("id");
        선릉역 = 지하철역_생성_요청("선릉역").jsonPath().getLong("id");
        왕십리역 = 지하철역_생성_요청("왕십리역").jsonPath().getLong("id");
        신촌역 = 지하철역_생성_요청("신촌역").jsonPath().getLong("id");
        종합운동장역 = 지하철역_생성_요청("종합운동장역").jsonPath().getLong("id");
        건대입구역 = 지하철역_생성_요청("건대입구역").jsonPath().getLong("id");
        사당역 = 지하철역_생성_요청("사당역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10);
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(강남역, 역삼역, 2, 2));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(역삼역, 선릉역, 3, 3));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(선릉역, 왕십리역, 1, 1));

        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(신촌역, 교대역, 50, 50));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(종합운동장역, 신촌역, 8, 8));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(건대입구역, 종합운동장역, 4, 4));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(사당역, 건대입구역, 4, 4));
    }

    @Nested
    @DisplayName("10km 이하")
    class NotMoreThan10 {
        @Test
        void distance2() {
            ExtractableResponse<Response> response = 경로_조회_요청(강남역, 역삼역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(2),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250)
            );
        }

        @Test
        void distance10() {
            ExtractableResponse<Response> response = 경로_조회_요청(교대역, 강남역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(10),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250)
            );
        }
    }

    @Nested
    @DisplayName("10km 초과, 50km 이하")
    class MoreThan10NotMoreThan50 {

        @Test
        void distance12() {
            ExtractableResponse<Response> response = 경로_조회_요청(교대역, 역삼역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(12),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_350)
            );
        }

        @Test
        void distance15() {
            ExtractableResponse<Response> response = 경로_조회_요청(교대역, 선릉역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(15),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_350)
            );
        }

        @Test
        void distance16() {
            ExtractableResponse<Response> response = 경로_조회_요청(교대역, 왕십리역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(16),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_450)
            );
        }
    }

    @Nested
    @DisplayName("50km 초과")
    class MoreThan50 {

        @Test
        void distance50() {
            ExtractableResponse<Response> response = 경로_조회_요청(교대역, 신촌역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(50),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_050)
            );
        }

        @Test
        void distance58() {
            ExtractableResponse<Response> response = 경로_조회_요청(교대역, 종합운동장역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(58),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_150)
            );
        }

        @Test
        void distance62() {
            ExtractableResponse<Response> response = 경로_조회_요청(교대역, 건대입구역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(62),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_250)
            );
        }

        @Test
        void distance66() {
            ExtractableResponse<Response> response = 경로_조회_요청(교대역, 사당역, SearchType.DURATION.name());

            assertAll(
                    () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(66),
                    () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_250)
            );
        }
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
