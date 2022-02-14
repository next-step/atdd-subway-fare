package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {

	static final String TYPE_DURATION = "DURATION";
	static final String TYPE_DISTANCE = "DISTANCE";

	public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
		return 두_역의_경로_조회를_요청(source, target, TYPE_DURATION, RestAssured.given().log().all());
	}

	public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return 두_역의_경로_조회를_요청(source, target, TYPE_DISTANCE, RestAssured.given().log().all());
	}

	public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target, RequestSpecification spec) {
		return 두_역의_경로_조회를_요청(source, target, TYPE_DURATION, spec);
	}

	public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, String type, RequestSpecification spec) {
		return spec
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.queryParam("source", source)
				.queryParam("target", target)
				.queryParam("type", type)
				.when().get("/paths")
				.then().log().all().extract();
	}

	public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraFare) {
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


	public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
		return 지하철_노선_생성_요청(name, color, upStation, downStation, distance, duration, 0);
	}

	public static Map<String, String> 구간_파라미터_생성(Long upStationId, Long downStationId, int distance, int duration) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", upStationId + "");
		params.put("downStationId", downStationId + "");
		params.put("distance", distance + "");
		params.put("duration", duration + "");
		return params;
	}

	// 검증 메소드
	public static void 경로_조회됨(ExtractableResponse<Response> response, Long... stationsId) {
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationsId);
	}

	public static void 경로_소요시간_조회됨(ExtractableResponse<Response> response, int duration) {
		assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
	}

	public static void 경로_거리_조회됨(ExtractableResponse<Response> response, int distance) {
		assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
	}

	public static void 지하철_요금_조회됨(ExtractableResponse<Response> response, int amount) {
		assertThat(response.jsonPath().getInt("fare")).isEqualTo(amount);
	}
}
