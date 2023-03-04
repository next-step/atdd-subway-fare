package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathSearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
	private Long 교대역;
	private Long 강남역;
	private Long 양재역;
	private Long 남부터미널역;
	private Long 청계산입구역;
	private Long 이호선;
	private Long 신분당선;
	private Long 삼호선;

	/**
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선* 900원 추가 요금
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재역
	 * 							|
	 * 						     청계산입구역
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
		남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
		청계산입구역 = 지하철역_생성_요청("청계산입구역").jsonPath().getLong("id");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 4);
		신분당선 = 추가요금이_있는_지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 4,900);
		삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5);
		삼호선 = 지하철_노선_생성_요청("1호선", "brue", 교대역, 남부터미널역, 2, 5);

		지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
		지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 청계산입구역, 3, 5));
	}

	@DisplayName("두 역의 최단 거리 경로를 조회한다.")
	@Test
	void findPathByDistance() {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

		// then
		assertAll(
				경로를_응답한다(response, 교대역, 남부터미널역, 양재역),
				경로의_거리를_응답한다(response, 5),
				경로의_소요_시간을_응답한다(response, 10)
		);
	}


	@DisplayName("두 역의 최단 시간 경로를 조회한다.")
	@Test
	void findPathByDuration() {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);

		// then
		assertAll(
				경로를_응답한다(response, 교대역, 강남역, 양재역),
				경로의_거리를_응답한다(response, 20),
				경로의_소요_시간을_응답한다(response, 8)
		);
	}

	@DisplayName("지하철 이용요금을 포함한 두 역의 최단 거리 경로를 조회한다.")
	@Test
	void findPathByDistanceWithFare() {
		// when
		ExtractableResponse<Response> response = 지하철_이용요금을_포함한_두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

		// then
		assertAll(
				경로를_응답한다(response, 교대역, 남부터미널역, 양재역),
				경로의_거리를_응답한다(response, 5),
				경로의_소요_시간을_응답한다(response, 10),
				경로의_이용_요금을_응답한다(response, 1250)
		);
	}

	/**
	 * When 출발역에서 도착역까지의 구간 길이가 10km 이내이고, 추가 요금이 있는 노선을 이용하면
	 * Then 총 지하철 요금은 기본 요금에 노선의 추가 요금이 더해진 금액이다.
	 */
	@DisplayName("지하철 구간 길이가 10km 이내이고 추가 요금이 있는 노선을 이용하면, 총 지하철 요금은 기본 요금에 노선의 추가 요금이 더해진 금액이다.")
	@Test
	void findPathByDistanceWithExtraFare() {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(양재역, 청계산입구역);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
	}

	private ExtractableResponse<Response> 지하철_이용요금을_포함한_두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return RestAssured
				.given().log().all()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/paths?source={sourceId}&target={targetId}&type={pathSearchType}", source, target, PathSearchType.DISTANCE)
				.then().log().all().extract();
	}

	private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return RestAssured
				.given().log().all()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/paths?source={sourceId}&target={targetId}&type={pathSearchType}", source, target, PathSearchType.DISTANCE)
				.then().log().all().extract();
	}

	private ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
		return RestAssured
				.given().log().all()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/paths?source={sourceId}&target={targetId}&type={pathSearchType}", source, target, PathSearchType.DURATION)
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

		return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
	}

	private Long 추가요금이_있는_지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraFare) {
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

	private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", upStationId + "");
		params.put("downStationId", downStationId + "");
		params.put("distance", distance + "");
		params.put("duration", duration + "");
		return params;
	}

	private Executable 경로를_응답한다(ExtractableResponse<Response> response, Long line1, Long line2, Long line3) {
		return () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(line1, line2, line3);
	}

	private static Executable 경로의_거리를_응답한다(ExtractableResponse<Response> response, int distance) {
		return () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
	}

	private static Executable 경로의_소요_시간을_응답한다(ExtractableResponse<Response> response, int duration) {
		return () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
	}

	private Executable 경로의_이용_요금을_응답한다(ExtractableResponse<Response> response, int fare) {
		return () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
	}
}
