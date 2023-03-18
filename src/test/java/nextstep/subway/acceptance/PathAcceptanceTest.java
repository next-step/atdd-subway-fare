package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static nextstep.subway.acceptance.common.CommonSteps.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
	private static final String TEENAGER_EMAIL = "teenager@email.com";
	private static final String CHILDREN_EMAIL = "children@email.com";
	private static final String PASSWORD = "password";
	private static Long 신논현역;
	private static Long 강남역;
	private static Long 양재역;
	private static Long 고속터미널역;
	private static Long 교대역;
	private static Long 남부터미널역;
	private Long 신분당선;
	private Long 이호선;
	private Long 삼호선;
	private Long 구호선;
	private String 청소년;
	private String 어린이;

	public static Stream<Arguments> providerOfPathWithinTenKM() {
		return Stream.of(
			Arguments.arguments(교대역, 양재역, 1_250),
			Arguments.arguments(양재역, 교대역, 1_250),
			Arguments.arguments(강남역, 남부터미널역, 1_250),
			Arguments.arguments(남부터미널역, 강남역, 1_250)
		);
	}

	public static Stream<Arguments> providerOfPathExceedingTenKMAndLessThanFiftyKM() {
		return Stream.of(
			Arguments.arguments(교대역, 신논현역, 1_750),
			Arguments.arguments(신논현역, 교대역, 1_750)
		);
	}

	public static Stream<Arguments> providerOfPathExceedingFiftyKM() {
		return Stream.of(
			Arguments.arguments(고속터미널역, 양재역, 2_150),
			Arguments.arguments(양재역, 고속터미널역, 2_150)
		);
	}

	public static Stream<Arguments> providerOfPathHasOneLineWithAdditionalFare() {
		return Stream.of(
			Arguments.arguments(교대역, 양재역, 1_750),
			Arguments.arguments(교대역, 강남역, 1_250),
			Arguments.arguments(고속터미널역, 신논현역, 2_250)
		);
	}

	public static Stream<Arguments> providerOfPathHasTwoOrMoreLineWithAdditionalFare() {
		return Stream.of(
			Arguments.arguments(남부터미널역, 신논현역, 2_850),
			Arguments.arguments(남부터미널역, 강남역, 1_750)
		);
	}

	/** line Fare
	 * 2 : 0
	 * 3 : 500
	 * 7 : 0
	 * 9 : 800
	 * 신분당 : 1000
	 */

	/** line
	 *	고속터미널역  --- *9호선* --- 신논현역
	 *  |                       |
	 *  *3호선*                   *신분당선*
	 *  |                       |
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재역
	 */

	/** distance
	 * 고속터미널역  --- 17 ---      신논현역
	 * |                          |
	 * 48                         25
	 * |                          |
	 * 교대역     --- 7 ---       강남역
	 * |                          |
	 * 2                          10
	 * |                          |
	 * 남부터미널역 --- 3 ---       양재역
	 */

	/** duration
	 * 고속터미널역  --- 17     --- 신논현역
	 * |                        |
	 * 25                       35
	 * |                        |
	 * 교대역     --- 5 ---       강남역
	 * |                          |
	 * 10                         5
	 * |                          |
	 * 남부터미널역 --- 3 ---       양재역
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
		고속터미널역 = 지하철역_생성_요청("고속터미널역").jsonPath().getLong("id");
		남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
		신논현역 = 지하철역_생성_요청("신논현역").jsonPath().getLong("id");
		교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 7, 5);
		신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 5);
		삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10);
		구호선 = 지하철_노선_생성_요청("9호선", "yellow", 고속터미널역, 신논현역, 17, 17);

		지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(고속터미널역, 교대역, 48, 25));
		지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(신논현역, 강남역, 25, 35));
		지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));

		청소년 = 베어러_인증_로그인_요청(TEENAGER_EMAIL, PASSWORD).jsonPath().getString("accessToken");
		어린이 = 베어러_인증_로그인_요청(CHILDREN_EMAIL, PASSWORD).jsonPath().getString("accessToken");
	}

	@DisplayName("두 역의 최단 거리 경로를 조회한다.")
	@Test
	void findPathByDistance() {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

		// then
		경로의_역_목록을_검증함(response, List.of(교대역, 남부터미널역, 양재역));
		시간_총합과_거리_총합이_조회됨(response, 13, 5);
	}

	@DisplayName("두 역의 최소 시간 경로를 조회한다.")
	@Test
	void findPathByDuration() {
		// when
		ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

		// then
		경로의_역_목록을_검증함(response, List.of(교대역, 강남역, 양재역));
		시간_총합과_거리_총합이_조회됨(response, 10, 17);
	}

	@DisplayName("두 역의 최단 거리 경로를 조회한다. - 실패 ")
	@Test
	void findPathByDistance_FAIL_SAME_SOURCE_AND_TARGET() {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 교대역);

		// then
		응답_상태_코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	@DisplayName("두 역의 최소 시간 경로를 조회한다. - 실패")
	@Test
	void findPathByDuration_FAIL_SAME_SOURCE_AND_TARGET() {
		// when
		ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 교대역);

		// then
		응답_상태_코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	@DisplayName("경로가 10KM 이내일 떄, 요금이 기본 운임 요금임을 검증한다.")
	@MethodSource("providerOfPathWithinTenKM")
	@ParameterizedTest
	void findPath_WITH_IN_10_KM_THEN_FARE_IS_BASIC(Long source, Long target, int fare) {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(source, target);

		// then
		경로의_요금을_검증함(response, fare);
	}

	@DisplayName("경로가 10KM 초과 50KM 이하 일 떄, 추가 운임이 올바르게 계산된 것을 검증한다.")
	@MethodSource("providerOfPathExceedingTenKMAndLessThanFiftyKM")
	@ParameterizedTest
	void findPath_EXCEEDING_10_KM_AND_LESS_THAN_50_KM(Long source, Long target, int fare) {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(source, target);

		// then
		경로의_요금을_검증함(response, fare);
	}

	@DisplayName("경로가 50KM 초과일 떄, 추가 운임이 올바르게 계산된 것을 검증한다.")
	@MethodSource("providerOfPathExceedingFiftyKM")
	@ParameterizedTest
	void findPath_EXCEEDING_50_KM(Long source, Long target, int fare) {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(source, target);

		// then
		경로의_요금을_검증함(response, fare);
	}

	@DisplayName("경로에 추가 운임이 있는 노선이 하나 있을 때, 추가 운임이 올바르게 계산된 것을 검증한다.")
	@MethodSource("providerOfPathHasOneLineWithAdditionalFare")
	@ParameterizedTest
	void findPath_HAS_ONE_LINE_WITH_ADDTIONAL_FARE(Long source, Long target, int fare) {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(source, target);

		// then
		경로의_요금을_검증함(response, fare);
	}

	@DisplayName("경로에 추가 운임이 있는 노선이 두 개 이상 있을 때, 추가 운임이 올바르게 계산된 것을 검증한다.")
	@MethodSource("providerOfPathHasTwoOrMoreLineWithAdditionalFare")
	@ParameterizedTest
	void findPath_HAS_TWO_MORE_LINE_WITH_ADDTIONAL_FARE(Long source, Long target, int fare) {
		// when
		ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(source, target);

		// then
		경로의_요금을_검증함(response, fare);
	}

	@DisplayName("청소년 사용자일 경우, 할인이 적용된 운임을 검증한다.")
	@Test
	void findPath_WHEN_USER_IS_TEENTAGER_THEN_DISCOUNT_FARE() {
		// when
		ExtractableResponse<Response> response = 로그인_유저의_두_역의_최단_거리_경로_조회를_요청(청소년, 교대역, 양재역);

		// then
		경로의_요금을_검증함(response, 720);
	}

	@DisplayName("어린이 사용자일 경우, 할인이 적용된 운임을 검증한다.")
	@Test
	void findPath_WHEN_USER_IS_CHILDREN_THEN_DISCOUNT_FARE() {
		// when
		ExtractableResponse<Response> response = 로그인_유저의_두_역의_최단_거리_경로_조회를_요청(어린이, 교대역, 양재역);

		// then
		경로의_요금을_검증함(response, 450);
	}
}
