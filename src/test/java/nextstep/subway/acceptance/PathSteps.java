package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;

public class PathSteps {
	public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", source)
			.queryParam("target", target)
			.queryParam("type", PathType.DISTANCE.name())
			.when().get("/paths")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", source)
			.queryParam("target", target)
			.queryParam("type", PathType.DURATION.name())
			.when().get("/paths")
			.then().log().all()
			.extract();
	}

	public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance,
		int duration) {
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

	public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance,
		int duration) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", upStationId + "");
		params.put("downStationId", downStationId + "");
		params.put("distance", distance + "");
		params.put("duration", duration + "");
		return params;
	}

	public static void 시간_총합과_거리_총합이_조회됨(ExtractableResponse<Response> response, int sumOfDuration, int sumOfDistance) {
		assertThat(response.jsonPath().getInt("duration")).isEqualTo(sumOfDuration);
		assertThat(response.jsonPath().getInt("distance")).isEqualTo(sumOfDistance);
	}

	public static void 경로의_역_목록을_검증함(ExtractableResponse<Response> response, List<Long> stations) {
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(
			stations.toArray(new Long[stations.size()]));
	}
}
