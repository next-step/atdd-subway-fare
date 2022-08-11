package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.domain.PathType.DISTANCE;
import static nextstep.subway.domain.PathType.DURATION;

public class PathSteps {

	public static Long 교대역;
	public static Long 강남역;
	public static Long 양재역;
	public static Long 남부터미널역;
	public static Long 이호선;
	public static Long 신분당선;
	public static Long 삼호선;

	public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return RestAssured
				.given().log().all()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, DISTANCE)
				.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
		return RestAssured
				.given().log().all()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, DURATION)
				.then().log().all().extract();
	}

	public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", upStationId + "");
		params.put("downStationId", downStationId + "");
		params.put("distance", distance + "");
		params.put("duration", duration + "");
		return params;
	}

}
