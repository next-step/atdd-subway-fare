package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.PathSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.PathSteps.강남역;
import static nextstep.subway.acceptance.PathSteps.교대역;
import static nextstep.subway.acceptance.PathSteps.남부터미널역;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.삼호선;
import static nextstep.subway.acceptance.PathSteps.신분당선;
import static nextstep.subway.acceptance.PathSteps.양재역;
import static nextstep.subway.acceptance.PathSteps.이호선;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 관련 테스트를 진행한다.")
public class FareAcceptanceTest extends AcceptanceTest{

	public static final int BASIC_DEDUCTION = 350;

	/**
	 * 교대역    --- *2호선(3km, 3분, 450원)* ---   강남역
	 * |                                     |
	 * *3호선(10km, 5분, 500원)*          *신분당선(2km, 2분, 900원)*
	 * |                                     |
	 * 남부터미널역 - *3호선(10km, 5분, 500원)* - 양재역
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		교대역 = 지하철역을_생성한다("교대역");
		강남역 = 지하철역을_생성한다("강남역");
		양재역 = 지하철역을_생성한다("양재역");
		남부터미널역 = 지하철역을_생성한다("남부터미널역");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 3, 3, 450);
		신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 2, 2, 900);
		삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 10, 5, 500);

		지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 10, 5));
	}

	/**
	 * When 최단 거리로 경로를 찾는다
	 * Then 추가 요금이 있는 경로를 이용하면 가장 큰 추가 요금만 요금에 추가된다.
	 */
	@DisplayName("추가 요금이 있는 노선으로 환승하면, 가장 큰 추가 요금만 이용 요금에 추가된다")
	@Test
	void 추가_요금이_있는_노선_중_가장_큰_추가요금만_요금이_추가된다() {
	    //when
		ExtractableResponse<Response> 응답 = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

		//then
		assertThat(응답.jsonPath().getLong("fare")).isEqualTo(Fare.BASIC_FARE + 900);
	}

	/**
	 * Given 청소년이 로그인되어 있다.
	 * When 최단 경로로 경로를 찾으면
	 * Then 할인된 요금이 반환된다.
	 */
	@DisplayName("청소년이 지하철을 이용하면, 할인된 요금이 적용된다")
	@Test
	void 청소년이_지하철을_이용하면_할인된_요금이_적용된다() {
	    //given
		String 청소년 = 로그인_되어_있음(TEENAGER_EMAIL, PASSWORD);

		//when
		ExtractableResponse<Response> 응답 = 두_역의_최단_거리_경로_조회를_요청(청소년, 교대역, 양재역);

		//then
		assertThat(응답.jsonPath().getLong("fare")).isEqualTo((Fare.BASIC_FARE + 900 - BASIC_DEDUCTION) * 0.8);
	}

	/**
	 * Given 어린이기 로그인되어 있다.
	 * When 최단 경로로 경로를 찾으면
	 * Then 할인된 요금이 반환된다.
	 */
	@DisplayName("어린이가 지하철을 이용하면, 할인된 요금이 적용된다")
	@Test
	void 어린이가_지하철을_이용하면_할인된_요금이_적용된다() {
	    //given
		String 어린이 = 로그인_되어_있음(CHILD_EMAIL, PASSWORD);

		//when
		ExtractableResponse<Response> 응답 = 두_역의_최단_거리_경로_조회를_요청(어린이, 교대역, 양재역);

		//then
		assertThat(응답.jsonPath().getLong("fare")).isEqualTo((Fare.BASIC_FARE + 900 - BASIC_DEDUCTION) * 0.5);
	}

	private long 지하철역을_생성한다(String name) {
		return 지하철역_생성_요청(관리자, name).jsonPath().getLong("id");
	}

	private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int surCharge) {
		Map<String, String> lineCreateParams;
		lineCreateParams = new HashMap<>();
		lineCreateParams.put("name", name);
		lineCreateParams.put("color", color);
		lineCreateParams.put("upStationId", upStation + "");
		lineCreateParams.put("downStationId", downStation + "");
		lineCreateParams.put("distance", distance + "");
		lineCreateParams.put("duration", duration + "");
		lineCreateParams.put("surCharge", surCharge + "");

		return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
	}
}
