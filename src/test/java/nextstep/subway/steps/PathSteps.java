package nextstep.subway.steps;

import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static nextstep.subway.steps.LineSteps.*;
import static nextstep.subway.steps.SectionSteps.지하철_노선에_지하철_구간_생성_요청;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SearchType;

public class PathSteps {

	public static ExtractableResponse<Response> 타입에_따라_두_역의_경로_조회를_요청(Long source, Long target, SearchType type) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 타입에_따라_두_역의_경로_조회를_요청(String accessToken, Long source, Long target, SearchType type) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
			.then().log().all()
			.extract();
	}

	public static Long 교대역;
	public static Long 강남역;
	public static Long 양재역;
	public static Long 남부터미널역;

	public static Long A_Station;
	public static Long B_Station;
	public static Long C_Station;
	public static Long D_Station;

	public static Long E_Station;
	public static Long F_Station;
	public static Long G_Station;

	public static Long Z_Station;
	public static Long Y_Station;
	public static Long X_Station;
	public static Long W_Station;

	public static Long 이호선;
	public static Long 신분당선;
	public static Long 삼호선;

	public static Long AD_Line;
	public static Long ZW_Line;

	public static void setup() {
		교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
		강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
		양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
		남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2).jsonPath().getLong("id");
		신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3).jsonPath().getLong("id");
		삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10).jsonPath().getLong("id");

		지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 3, 10);

		A_Station = 지하철역_생성_요청("A").jsonPath().getLong("id");
		B_Station = 지하철역_생성_요청("B").jsonPath().getLong("id");
		C_Station = 지하철역_생성_요청("C").jsonPath().getLong("id");
		D_Station = 지하철역_생성_요청("D").jsonPath().getLong("id");

		E_Station = 지하철역_생성_요청("E").jsonPath().getLong("id");
		F_Station = 지하철역_생성_요청("F").jsonPath().getLong("id");
		G_Station = 지하철역_생성_요청("G").jsonPath().getLong("id");

		Z_Station = 지하철역_생성_요청("Z").jsonPath().getLong("id");
		Y_Station = 지하철역_생성_요청("Y").jsonPath().getLong("id");
		X_Station = 지하철역_생성_요청("X").jsonPath().getLong("id");
		W_Station = 지하철역_생성_요청("W").jsonPath().getLong("id");

		AD_Line = 지하철_노선_생성_요청("AD", "black", A_Station, B_Station, 25, 5).jsonPath().getLong("id");
		ZW_Line = 지하철_노선_생성_요청("ZW", "white", D_Station, Z_Station, 10, 7, 1000).jsonPath().getLong("id");

		지하철_노선에_지하철_구간_생성_요청(AD_Line, B_Station, C_Station, 25, 5);
		지하철_노선에_지하철_구간_생성_요청(AD_Line, C_Station, D_Station, 30, 10);

		지하철_노선에_지하철_구간_생성_요청(AD_Line, D_Station, E_Station, 20, 10);
		지하철_노선에_지하철_구간_생성_요청(AD_Line, E_Station, F_Station, 20, 10);
		지하철_노선에_지하철_구간_생성_요청(AD_Line, F_Station, G_Station, 20, 10);

		지하철_노선에_지하철_구간_생성_요청(ZW_Line, Z_Station, Y_Station, 5, 7);
		지하철_노선에_지하철_구간_생성_요청(ZW_Line, Y_Station, X_Station, 5, 7);
		지하철_노선에_지하철_구간_생성_요청(ZW_Line, X_Station, W_Station, 5, 7);
		지하철_노선에_지하철_구간_생성_요청(ZW_Line, W_Station, G_Station, 10, 7);
	}
}
