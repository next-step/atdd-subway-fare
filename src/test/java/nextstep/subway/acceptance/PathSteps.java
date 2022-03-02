package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.Steps;
import nextstep.subway.domain.WeightType;

public class PathSteps extends Steps {
	public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, WeightType weightType) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", source)
			.queryParam("target", target)
			.queryParams("weightType", weightType)
			.when().get("/paths")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 로그인_후_두_역의_경로_조회를_요청(String accessToken, Long source, Long target, WeightType weightType) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", source)
			.queryParam("target", target)
			.queryParams("weightType", weightType)
			.when().get("/paths")
			.then().log().all().extract();
	}

	public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int surcharge) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("upStationId", upStation + "");
		params.put("downStationId", downStation + "");
		params.put("distance", distance + "");
		params.put("duration", duration + "");
		params.put("surcharge", surcharge + "");
		return LineSteps.지하철_노선_생성_요청(params).jsonPath().getLong("id");
	}

	public static Map<String, String> 구간_파라메터(Long upStationId, Long downStationId, int distance, int duration) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", upStationId + "");
		params.put("downStationId", downStationId + "");
		params.put("distance", distance + "");
		params.put("duration", duration + "");
		return params;
	}

	public static void 지하철_경로_역_확인(ExtractableResponse<Response> response, Long ...stationIds) {
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
	}

	public static void 경로_거리_확인(ExtractableResponse<Response> response, int distance) {
		assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
	}

	public static void 소요_시간_확인(ExtractableResponse<Response> response, int duration) {
		assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
	}

	public static void 요금_확인(ExtractableResponse<Response> response, int fare) {
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
	}
}
