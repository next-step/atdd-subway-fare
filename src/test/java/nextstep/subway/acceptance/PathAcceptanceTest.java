package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.applicaion.dto.SearchType.*;
import static nextstep.subway.steps.PathSteps.*;
import static nextstep.subway.steps.SectionSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
	/**
	 * 				(di:10, dr:2)		(di:5, dr:1)
	 * 교대역		---	*2호선*	---	강남역	---		역삼역
	 * |								|
	 * *3호선*						*신분당선*
	 * (di:2, dr:10)				(di:10, dr:3)
	 * |								|
	 * 남부터미널역	---	*3호선*	---	양재역
	 * 				(di:3, dr:10)
	 */
	/**
	 *   (25, 5)   (25, 5)  (30, 10)   (20, 10)	 (20, 10)  (20, 10)
	 * A --- --- B --- --- C --- --- D --- --- E --- --- F --- --- G
	 * 								 |							   |
	 * 							  (10, 7)	 					(10, 7)
	 * 								 |							   |
	 * 								 Z ㅡㅡㅡㅡ Y ㅡㅡㅡㅡ X ㅡㅡㅡㅡㅡ W
	 * 								   (5, 7)    (5, 7)    (5, 7)
	 * A ... G 호선 : 무료
	 * D - Z ... W - G 호선 : 추가요금 1000원
	 * */
	@BeforeEach
	public void setUp() {
		super.setUp();
		setup();
	}

	/**
	 * When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청하면
	 * Then 최단 거리 기준 경로, 총 거리, 소요 시간을 응답
	 * **/
	@DisplayName("두 역의 최단 거리 경로를 조회")
	@Test
	void findPathByDistance() {
		// when
		ExtractableResponse<Response> response = 타입에_따라_두_역의_경로_조회를_요청(교대역, 양재역, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(교대역, 남부터미널역, 양재역),
			() -> assertThat(totalDistance).isEqualTo(5),
			() -> assertThat(totalDuration).isEqualTo(20)
		);
	}

	/**
	 * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
	 * Then 최소 시간 기준 경로, 총 거리, 소요 시간을 응답
	 * **/
	@DisplayName("두 역의 최소 시간 경로를 조회")
	@Test
	void findPathByTime() {
		// when
		ExtractableResponse<Response> response = 타입에_따라_두_역의_경로_조회를_요청(교대역, 양재역, DURATION);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(교대역, 강남역, 양재역),
			() -> assertThat(totalDistance).isEqualTo(20),
			() -> assertThat(totalDuration).isEqualTo(5)
		);
	}

	/**
	 * When 출발역에서 도착역까지의 최단 거리가 10km 이내의 경로를 조회하면
	 * Then 최단 거리, 총 거리, 소요 시간, 1250원의 이용요금을 응답
	 * */
	@DisplayName("이용 거리가 10km 이내의 경로 조회")
	@Test
	void path10kmUnder() {
		// when
		ExtractableResponse<Response> response = 타입에_따라_두_역의_경로_조회를_요청(교대역, 양재역, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		int fare = response.jsonPath().getInt("fare");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(교대역, 남부터미널역, 양재역),
			() -> assertThat(totalDistance).isEqualTo(5),
			() -> assertThat(totalDuration).isEqualTo(20),
			() -> assertThat(fare).isEqualTo(1250)
		);
	}

	/**
	 * When 출발역에서 도착역까지의 최단 거리가 15km 이내의 경로를 조회하면
	 * Then 최단 거리, 총 거리, 소요 시간
	 * And 5km 마다 100원 추가로 1350원의 이용요금을 응답
	 * */
	@DisplayName("이용 거리가 15km 이내의 경로 조회")
	@Test
	void path15kmUnder() {
		// when
		ExtractableResponse<Response> response = 타입에_따라_두_역의_경로_조회를_요청(남부터미널역, 강남역, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		int fare = response.jsonPath().getInt("fare");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(남부터미널역, 교대역, 강남역),
			() -> assertThat(totalDistance).isEqualTo(12),
			() -> assertThat(totalDuration).isEqualTo(12),
			() -> assertThat(fare).isEqualTo(1350)
		);
	}

	/**
	 * When 출발역에서 도착역까지의 최단 거리가 20km 이내의 경로를 조회하면
	 * Then 최단 거리, 총 거리, 소요 시간
	 * And 5km 마다 100원 추가로 1450원의 이용요금을 응답
	 * */
	@DisplayName("이용 거리가 20km 이내의 경로 조회")
	@Test
	void path20kmUnder() {
		// given
		Long 역삼역 = 지하철역_생성_요청("역삼역").jsonPath().getLong("id");
		지하철_노선에_지하철_구간_생성_요청(이호선, 강남역, 역삼역, 5, 1);

		// when
		ExtractableResponse<Response> response = 타입에_따라_두_역의_경로_조회를_요청(남부터미널역, 역삼역, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		int fare = response.jsonPath().getInt("fare");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(남부터미널역, 교대역, 강남역, 역삼역),
			() -> assertThat(totalDistance).isEqualTo(17),
			() -> assertThat(totalDuration).isEqualTo(13),
			() -> assertThat(fare).isEqualTo(1450)
		);
	}

	/**
	 * When 출발역에서 도착역까지의 최단 거리가 50km 경로를 조회하면
	 * Then 최단 거리, 총 거리, 소요 시간
	 * And 5km 마다 100원 추가로 2050원의 이용요금을 응답
	 * */
	@DisplayName("이용 거리가 50km 경로 조회")
	@Test
	void path50km() {
		// when
		ExtractableResponse<Response> response = 타입에_따라_두_역의_경로_조회를_요청(A_Station, C_Station, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		int fare = response.jsonPath().getInt("fare");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(A_Station, B_Station, C_Station),
			() -> assertThat(totalDistance).isEqualTo(50),
			() -> assertThat(totalDuration).isEqualTo(10),
			() -> assertThat(fare).isEqualTo(2050)
		);
	}

	/**
	 * When 출발역에서 도착역까지의 최단 거리가 80km 경로를 조회하면
	 * Then 최단 거리, 총 거리, 소요 시간
	 * And 50km 초과시 기본 요금 + 8km 마다 100원 추가로 2150원의 이용요금
	 * */
	@DisplayName("이용 거리가 80km 경로 조회")
	@Test
	void path106km() {
		// when
		ExtractableResponse<Response> response = 타입에_따라_두_역의_경로_조회를_요청(A_Station, D_Station, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		int fare = response.jsonPath().getInt("fare");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(A_Station, B_Station, C_Station, D_Station),
			() -> assertThat(totalDistance).isEqualTo(80),
			() -> assertThat(totalDuration).isEqualTo(20),
			() -> assertThat(fare).isEqualTo(2450)
		);
	}

	/**
	 * When 출발역에서 도착역까지 최단 거리의 경로를 조회하면
	 * And  추가 요금이 있는 노선을 이용하고
	 * Then 추가 요금이 계산된 이용요금을 응답
	 * */
	@DisplayName("추가 요금 노선 경로 조회")
	@Test
	void addFareTest() {
		// when
		ExtractableResponse<Response> response = 타입에_따라_두_역의_경로_조회를_요청(D_Station, G_Station, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		int fare = response.jsonPath().getInt("fare");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(D_Station, Z_Station, Y_Station, X_Station, W_Station, G_Station),
			() -> assertThat(totalDistance).isEqualTo(35),
			() -> assertThat(totalDuration).isEqualTo(35),
			() -> assertThat(fare).isEqualTo(2750)
		);
	}

	/**
	 * Given 13세 이상 ~ 19세 미만의 청소년 회원이
	 * When 출발역에서 도착역까지 최단 거리의 경로를 조회하면
	 * Then 최단 거리, 소요 시간,
	 * And 청소년 할인이 ((이용요금 - 350) * 0.8) 적용된 이용요금을 응답
	 * */
	@DisplayName("청소년 이용 요금")
	@Test
	void youthFareTest() {
		// given
		String youthToken = 회원_생성_요청("youth@email.com", "passwd", 16).jsonPath().getString("accessToken");

		// when
		ExtractableResponse<Response> response = 회원_타입에_따라_두_역의_경로_조회를_요청(youthToken, D_Station, G_Station, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		int fare = response.jsonPath().getInt("fare");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(D_Station, Z_Station, Y_Station, X_Station, W_Station, G_Station),
			() -> assertThat(totalDistance).isEqualTo(35),
			() -> assertThat(totalDuration).isEqualTo(35),
			() -> assertThat(fare).isEqualTo(1920)
		);
	}

	/**
	 * Given 6세 이상 ~ 13세 미만의 어린이 회원이
	 * When 출발역에서 도착역까지 최단 거리의 경로를 조회하면
	 * Then 최단 거리, 소요 시간,
	 * And 어린이 할인이 ((이용요금 - 350) * 0.5) 적용된 이용요금을 응답
	 * */
	@DisplayName("어린이 이용 요금")
	@Test
	void kidsFareTest() {
		// given
		String kidsToken = 회원_생성_요청("kids@email.com", "passwd", 10).jsonPath().getString("accessToken");

		// when
		ExtractableResponse<Response> response = 회원_타입에_따라_두_역의_경로_조회를_요청(kidsToken, D_Station, G_Station, DISTANCE);

		// then
		List<Long> stationsIds = response.jsonPath().getList("stations.id", Long.class);
		int totalDistance = response.jsonPath().getInt("distance");
		int totalDuration = response.jsonPath().getInt("duration");
		int fare = response.jsonPath().getInt("fare");
		assertAll(
			() -> assertThat(stationsIds).containsExactly(D_Station, Z_Station, Y_Station, X_Station, W_Station, G_Station),
			() -> assertThat(totalDistance).isEqualTo(35),
			() -> assertThat(totalDuration).isEqualTo(35),
			() -> assertThat(fare).isEqualTo(1200)
		);
	}
}
