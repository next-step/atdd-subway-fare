package nextstep.subway.acceptance;

import nextstep.subway.domain.PathFindType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {

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
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 500);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 10));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        var response = 두_역의_최단_경로_조회를_요청(교대역, 양재역, PathFindType.DISTANCE);

        assertThat(response.jsonPath().getList("stations.id", Long.class))
            .containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(20);
        // 삼호선에 추가요금 500원이 등록되어 있다
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1750);
    }

    /**
     * Given 지하철 노선에 역이 등록되어 있음 When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청 Then 최소 시간 기준 경로를 응답 And 총
     * 거리와 소요 시간을 함께 응답 And 지하철 이용 요금도 함께 응답
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        var response = 두_역의_최단_경로_조회를_요청(교대역, 양재역, PathFindType.DURATION);

        assertThat(response.jsonPath().getList("stations.id", Long.class))
            .containsExactly(교대역, 강남역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(20);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1450);
    }

    /**
     * Given 추가요금이 있는 노선이 있고 And 해당 노선에 역들이 등록되어 있고 When 출발역에서 도착역까지의 경로 조회를 요청한다 Then 최소 시간 기준 경로를
     * 응답하고 And 추가요금이 포함된 지하철 이용 요금도 함께 응답한다
     */
    @DisplayName("900원 추가요금이 있는 노선의 경로를 조회한다")
    @ParameterizedTest
    @CsvSource(value = {"10,2150", "11,2250", "15,2250"})
    void findPathWithAdditionalFare(int distance, int fare) {
        var 도곡역 = 지하철역_생성_요청("도곡역").jsonPath().getLong("id");
        var 선릉역 = 지하철역_생성_요청("선릉역").jsonPath().getLong("id");
        지하철_노선_생성_요청("분당선", "yellow", 도곡역, 선릉역, distance, 3, 900);

        var response = 두_역의_최단_경로_조회를_요청(도곡역, 선릉역, PathFindType.DURATION);

        assertThat(response.jsonPath().getList("stations.id", Long.class))
            .containsExactly(도곡역, 선릉역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }

    /*
     * Given 더 적은 추가요금이 있는 노선이 있고
     * And 연결되는 역이 있는 추가요금이 더 비싼 노선이 있고
     * When 저렴한 추가요금 노선의 역에서 출발해서 추가요금이 비싼 노선의 역에 도착하는 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 응답하고
     * And 비싼 추가요금이 적용된 지하철 이용 요금을 함께 응답한다
     * */
    // 남부터미널역 - 도곡역 - 양재역
    //             |
    //            선릉역
    @DisplayName("환승 후 노선 경로 조회할 경우 추가 금액이 가장 높은 추가 요금을 적용한다")
    @ParameterizedTest
    @CsvSource(value = {"9,2150", "10,2250", "14,2250"})
    void findPathWithLargestAdditionalFareWhenTransport(int distance, int fare) {
        var 도곡역 = 지하철역_생성_요청("도곡역").jsonPath().getLong("id");
        var 선릉역 = 지하철역_생성_요청("선릉역").jsonPath().getLong("id");
        지하철_노선_생성_요청("분당선", "yellow", 도곡역, 선릉역, distance, 3, 900);
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 도곡역, 2, 10));

        var response = 두_역의_최단_경로_조회를_요청(양재역, 선릉역, PathFindType.DURATION);

        assertThat(response.jsonPath().getList("stations.id", Long.class))
            .containsExactly(양재역, 도곡역, 선릉역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }

    /**
     * Given 노선이 있고 When 어린이 사용자가 경로 조회를 요청하면 Then 할인이 적용된 지하철 이용 요금을 함께 응답한다
     */
    @DisplayName("어린이 요금자는 경로 조회시 할인된 금액을 적용한다")
    @Test
    // age에 대해 검사하기 위해 ParameterizedTest를 수행할까 했으나
    // 인수테스트에선 Happy case만 검사하고 자세한 정책은 단위 테스트에서 하는 것이 더 좋겠다고 판단
    // 위에서도 그렇게 할걸...
    void findPathWithKidDiscountPolicy() {
        var response = 두_역의_최단_경로_조회를_요청(교대역, 양재역, PathFindType.DURATION, 어린이);

        assertThat(response.jsonPath().getList("stations.id", Long.class))
            .containsExactly(교대역, 강남역, 양재역);
        // Math.round((1450 / (double)(50/100))) 적용 => 실제 프로젝트였으면 BigDecimal로 계산해야 함
        // 여기서 1450은 20km이므로 기본 운임료 + 10km(추가요금 200원) 1250 + 200
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(725);
    }

    /**
     * Given 노선이 있고 When 청소년 사용자가 경로 조회 요청하면 Then 할인이 적용된 지하철 이용 요금을 함께 응답한다
     */
    @DisplayName("청소년 요금자는 경로 조회시 할인된 금액을 적용한다")
    @Test
    void findPathWithTeenagerDiscountPolicy() {
        var response = 두_역의_최단_경로_조회를_요청(교대역, 양재역, PathFindType.DURATION, 청소년);

        assertThat(response.jsonPath().getList("stations.id", Long.class))
            .containsExactly(교대역, 강남역, 양재역);
        // Math.round((1450 / (double)(80/100))) 적용 => 실제 프로젝트였으면 BigDecimal로 계산해야 함
        // 여기서 1450은 20km이므로 기본 운임료 + 10km(추가요금 200원) 1250 + 200
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1823);
    }

    private Long 지하철_노선_생성_요청(
        String name,
        String color,
        Long upStation,
        Long downStation,
        int distance,
        int duration,
        int extraFare
    ) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("extraFare", extraFare + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    private Long 지하철_노선_생성_요청(
        String name,
        String color,
        Long upStation,
        Long downStation,
        int distance,
        int duration
    ) {
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

    private Map<String, String> createSectionCreateParams(
        Long upStationId,
        Long downStationId,
        int distance,
        int duration
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
