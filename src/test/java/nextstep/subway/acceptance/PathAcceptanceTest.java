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
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
	private Long 교대역;
	private Long 강남역;
	private Long 양재역;
	private Long 남부터미널역;
	private Long 판교역;
	private Long 경기광주역;
	private Long 곤지암역;
	private Long 이호선;
	private Long 신분당선;
	private Long 삼호선;
	private Long 경강선;

	/**
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선* 900원 추가 요금
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재역
	 * 							|
	 * 						     판교역 -------*경강선* 1200원 추가 요금------경기광주역------곤지암역
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
		남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
		판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
		경기광주역 = 지하철역_생성_요청("경기광주역").jsonPath().getLong("id");
		곤지암역 = 지하철역_생성_요청("곤지암역").jsonPath().getLong("id");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 4);
		신분당선 = 추가요금이_있는_지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 4,900);
		경강선 = 추가요금이_있는_지하철_노선_생성_요청("경강선", "black", 판교역, 경기광주역, 4, 4,1200);
		삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5);

		지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
		지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 판교역, 3, 5));
		지하철_노선에_지하철_구간_생성_요청(경강선, createSectionCreateParams(경기광주역, 곤지암역, 50, 5));
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
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(양재역, 판교역);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
	}

	/**
	 * When 출발역에서 도착역까지의 구간 길이가 10km 이내이고, 추가 요금이 있는 여러 노선을 이용하면
	 * Then 총 지하철 요금은 (기본 요금 + 가장 큰 노선 추가 요금) 이다.
	 */
	@DisplayName("구간 길이가 10km 이내이고 여러 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 가장 큰 노선 추가 요금) 이다.")
	@Test
	void findPathByDistanceWithExtraFares() {
		// when
		var response = 두_역의_최단_거리_경로_조회를_요청(양재역, 경기광주역);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(2450);
	}

	/**
	 * When 출발역에서 도착역까지의 구간 길이가 10km 초과 50km 이내이고, 추가 요금이 있는 노선을 이용하면
	 * Then 총 지하철 요금은 (기본 요금 + 추가 요금(5km 당 100원) + 노선 추가 요금) 이다.
	 */
	@DisplayName("구간 길이가 10km 초과 50km 이내이고 여러 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 추가 요금(5km 당 100원) + 가장 큰 노선 추가 요금) 이다.")
	@Test
	void findPathByMiddleDistanceWithExtraFare() {
		// when
		var response = 두_역의_최단_거리_경로_조회를_요청(강남역, 판교역);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250);
	}

	/**
	 * When 출발역에서 도착역까지의 구간 길이가 10km 초과 50km 이내이고, 추가 요금이 있는 여러 노선을 이용하면
	 * Then 총 지하철 요금은 (기본 요금 + 추가 요금(5km 당 100원) + 가장 큰 노선 추가 요금) 이다.
	 */
	@DisplayName("구간 길이가 10km 초과 50km 이내이고 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 추가 요금(5km 당 100원) + 노선 추가 요금) 이다.")
	@Test
	void findPathByMiddleDistanceWithExtraFares() {
		// when
		var response = 두_역의_최단_거리_경로_조회를_요청(강남역, 경기광주역);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(2650);
	}

	/**
	 * When 출발역에서 도착역까지의 구간 길이가 50km 초과이고, 추가 요금이 있는 노선을 이용하면
	 * Then 총 지하철 요금은 (기본 요금 + 추가 요금(8km 당 100원) + 노선 추가 요금) 이다.
	 */
	@DisplayName("구간 길이가 50km 초과이고 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 추가 요금(8km 당 100원) + 노선 추가 요금) 이다.")
	@Test
	void longDistanceWithExtraFare() {
		// when
		var response = 두_역의_최단_거리_경로_조회를_요청(판교역, 곤지암역);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(3350);
	}

	/**
	 * When 출발역에서 도착역까지의 구간 길이가 50km 초과이고, 추가 요금이 있는 여러 노선을 이용하면
	 * Then 총 지하철 요금은 (기본 요금 + 추가 요금(8km 당 100원) + 가장 큰 노선 추가 요금) 이다.
	 */
	@DisplayName("구간 길이가 50km 초과이고 여러 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 추가 요금(8km 당 100원) + 가장 큰 노선 추가 요금) 이다.")
	@Test
	void longDistanceWithExtraFares() {
		// when
		var response = 두_역의_최단_거리_경로_조회를_요청(양재역, 곤지암역);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(3350);
	}

	/**
	 * Given 성인 사용자가 로그인을 요청하고
	 * When 지하철 경로 조회를 요청한 로그인 사용자가 성인이라면
	 * Then 지하철 요금은 할인되지 않은 요금이다.
	 */
	@DisplayName("로그인 사용자가 성인이라면, 지하철 요금은 할인되지 않은 요금이다.")
	@Test
	void findPathByAdultMember() {
		// given
		var accessToken = 베어러_인증_로그인_요청("adult@email.com", "password").jsonPath().getString("accessToken");

		// when
		var response = 로그인_사용자가_두_역의_최단_거리_경로_조회를_요청(교대역, 남부터미널역, accessToken);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
	}

	/**
	 * Given 청소년 사용자가 로그인을 요청하고
	 * When 지하철 경로 조회를 요청한 로그인 사용자가 청소년이라면
	 * Then 지하철 요금은 운임에서 350원을 공제한 금액의 20%가 할인된 요금이다.
	 */
	@DisplayName("로그인 사용자가 청소년이라면, 지하철 요금은 운임에서 350원을 공제한 금액의 20%가 할인된 요금이다.")
	@Test
	void findPathByTeenagerMember() {
		// given
		var accessToken = 베어러_인증_로그인_요청("teenager@email.com", "password").jsonPath().getString("accessToken");

		// when
		var response = 로그인_사용자가_두_역의_최단_거리_경로_조회를_요청(교대역, 남부터미널역, accessToken);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(1070);
	}

	/**
	 * Given 어린이 사용자가 로그인을 요청하고
	 * When 지하철 경로 조회를 요청한 로그인 사용자가 어린이이라면
	 * Then 지하철 요금은 운임에서 350원을 공제한 금액의 50%가 할인된 요금이다.
	 */
	@DisplayName("로그인 사용자가 어린이라면, 지하철 요금은 운임에서 350원을 공제한 금액의 50%가 할인된 요금이다.")
	@Test
	void findPathByChildMember() {
		// given
		var accessToken = 베어러_인증_로그인_요청("child@email.com", "password").jsonPath().getString("accessToken");

		// when
		var response = 로그인_사용자가_두_역의_최단_거리_경로_조회를_요청(교대역, 남부터미널역, accessToken);

		// then
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(800);
	}

	private ExtractableResponse<Response> 로그인_사용자가_두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String accessToken) {
		return RestAssured
				.given().log().all()
				.auth().oauth2(accessToken)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/paths?source={sourceId}&target={targetId}&type={pathSearchType}", source, target, PathSearchType.DISTANCE)
				.then().log().all().extract();
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
