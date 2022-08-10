package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.domain.PathType.DISTANCE;
import static nextstep.subway.domain.PathType.DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
	private Long 교대역;
	private Long 강남역;
	private Long 양재역;
	private Long 남부터미널역;

    private Long 이호선;
    private Long 삼호선;
    private Long 신분당선;

	@BeforeEach
	public void setUp() {
		super.setUp();

		교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
		강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
		남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
	}

	/**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
	 * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
	 * Then 최단 거리 경로를 응답(총 거리 10km 이하)
	 * And 총 거리와 소요 시간을 함께 응답함
	 * And 지하철 이용 요금도 함께 응답함
	 */
	@DisplayName("최단 거리의 거리가 10km 이하 일 때 두 역의 최단 거리 경로를 조회한다.")
	@Test
	void 최단_거리의_거리가_10km_이하_일_때_최단_거리_경로_조회() {
        /**
         * 교대역    --- *2호선(10km, 5분)* ---   강남역
         * |                                     |
         * *3호선(2km, 1분)*               *신분당선(10km, 5분)*
         * |                                     |
         * 남부터미널역  --- *3호선(3km, 2분)* ---   양재역
         */
        //given
        이호선 = 이호선에_교대역_강남역_구간을_생성한다(10, 5);
        신분당선 = 신분당선에_강남역_양재역_구간을_생성한다(10, 5);
        삼호선 = 삼호선에_교대역_남부터미널역_구간을_생성한다(2, 1);

        삼호선에_남부터미널역_양재역_구간을_추가한다(3, 2);

		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

		// then
        응답을_검증한다(response, 남부터미널역, 5, 3, 1250);
    }

	/**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
	 * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
	 * Then 최단 거리 경로를 응답(총 거리 50km 이하)
	 * And 총 거리와 소요 시간을 함께 응답함
	 * And 지하철 이용 요금도 함께 응답함
	 */
	@DisplayName("최단 거리의 거리가 50km 이하 일 때 두 역의 최단 거리 경로를 조회한다.")
	@Test
	void 최단_거리의_거리가_50km_이하_일_때_최단_거리_경로_조회() {
        /**
         * 교대역    --- *2호선(25km, 12분)* ---   강남역
         * |                                     |
         * *3호선(100km, 50분)*               *신분당선(25km, 12분)*
         * |                                     |
         * 남부터미널역  --- *3호선(100km, 50분)* ---   양재역
         */
        //given
        이호선 = 이호선에_교대역_강남역_구간을_생성한다(25, 12);
        신분당선 = 신분당선에_강남역_양재역_구간을_생성한다(25, 12);
        삼호선 = 삼호선에_교대역_남부터미널역_구간을_생성한다(100, 50);

        삼호선에_남부터미널역_양재역_구간을_추가한다(100, 50);

		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

		// then
        응답을_검증한다(response, 강남역, 50, 24, 2050);
    }

	/**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
	 * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
	 * Then 최단 거리 경로를 응답(총 거리 50km 이상)
	 * And 총 거리와 소요 시간을 함께 응답함
	 * And 지하철 이용 요금도 함께 응답함
	 */
	@DisplayName("최단 거리의 거리가 50km 이상 일 때 두 역의 최단 거리 경로를 조회한다.")
	@Test
	void 최단_거리의_거리가_50km_이상_일_때_최단_거리_경로_조회() {
        /**
         * 교대역    --- *2호선(75km, 35분)* ---   강남역
         * |                                     |
         * *3호선(60km, 30분)*               *신분당선(75km, 35분)*
         * |                                     |
         * 남부터미널역  --- *3호선(60km, 30분)* ---   양재역
         */
        //given
        이호선 = 이호선에_교대역_강남역_구간을_생성한다(75, 35);
        신분당선 = 신분당선에_강남역_양재역_구간을_생성한다(75, 35);
        삼호선 = 삼호선에_교대역_남부터미널역_구간을_생성한다(60, 30);

        삼호선에_남부터미널역_양재역_구간을_추가한다(60, 30);
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

		// then
        응답을_검증한다(response, 강남역, 120, 60, 2950);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
	 * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
	 * Then 최소 시간 기준 경로를 응답
	 * And 총 거리와 소요 시간을 함께 응답함
	 */
	@DisplayName("두 역의 최소 시간 경로를 조회한다.")
	@Test
	void 최소_시간_경로_찾기() {
        /**
         * 교대역    --- *2호선(10km, 5분)* ---   강남역
         * |                                     |
         * *3호선(2km, 1분)*               *신분당선(10km, 5분)*
         * |                                     |
         * 남부터미널역  --- *3호선(3km, 2분)* ---   양재역
         */
        //given
        Long 이호선 = 이호선에_교대역_강남역_구간을_생성한다(10, 5);
        Long 신분당선 = 신분당선에_강남역_양재역_구간을_생성한다(10, 5);
        Long 삼호선 = 삼호선에_교대역_남부터미널역_구간을_생성한다(2, 1);

        삼호선에_남부터미널역_양재역_구간을_추가한다(3, 2);

		//when
		ExtractableResponse<Response> 응답 = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

		//then
		assertAll(
				() -> assertThat(응답.jsonPath().getLong("distance")).isEqualTo(5),
				() -> assertThat(응답.jsonPath().getLong("duration")).isEqualTo(3)
		);
	}

	private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, DISTANCE)
				.then().log().all().extract();
	}

	private ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
		return given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, DURATION)
				.then().log().all().extract();
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

		return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
	}

	private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", upStationId + "");
		params.put("downStationId", downStationId + "");
		params.put("distance", distance + "");
		params.put("duration", duration + "");
		return params;
	}

    private Long 삼호선에_교대역_남부터미널역_구간을_생성한다(int distance, int duration) {
        return 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, distance, duration);
    }

    private Long 신분당선에_강남역_양재역_구간을_생성한다(int distance, int duration) {
        return 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, distance, duration);
    }

    private Long 이호선에_교대역_강남역_구간을_생성한다(int distance, int duration) {
        return 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, distance, duration);
    }

    private ExtractableResponse<Response> 삼호선에_남부터미널역_양재역_구간을_추가한다(int distance, int duration) {
        return 지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, distance, duration));
    }

    private void 응답을_검증한다(ExtractableResponse<Response> response, Long middleStation, int distance, int duration, int price) {
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, middleStation, 양재역),
                () -> assertThat(response.jsonPath().getLong("distance")).isEqualTo(distance),
                () -> assertThat(response.jsonPath().getLong("duration")).isEqualTo(duration),
                () -> assertThat(response.jsonPath().getLong("price")).isEqualTo(price)
        );
    }
}
