package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static nextstep.subway.acceptance.common.CommonSteps.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
	private Long 교대역;
	private Long 강남역;
	private Long 양재역;
	private Long 남부터미널역;
	private Long 이호선;
	private Long 신분당선;
	private Long 삼호선;

	/** line
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재역
	 */

	/** distance
	 * 교대역     --- 10 ---       강남역
	 * |                          |
	 * 2                          10
	 * |                          |
	 * 남부터미널역 --- 3 ---       양재역
	 */

	/** duration
	 * 교대역     --- 5 ---       강남역
	 * |                          |
	 * 10                         5
	 * |                          |
	 * 남부터미널역 --- 3 ---       양재역
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
		남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 5);
		신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 5);
		삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10);

		지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
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
		시간_총합과_거리_총합이_조회됨(response, 10, 20);
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
}
