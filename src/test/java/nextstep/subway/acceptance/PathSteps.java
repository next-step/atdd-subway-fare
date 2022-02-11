package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

	public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return requestPath(source, target, RestAssured.given().log().all());
	}

	private static ExtractableResponse<Response> requestPath(Long source, Long target, RequestSpecification requestSpecification) {
		return requestSpecification
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.queryParam("source", source)
				.queryParam("target", target)
				.when().get("/paths")
				.then().log().all().extract();
	}

	private static RequestSpecification createRestAssuredWithDocument(RequestSpecification documentSpecification) {
		return RestAssured
				.given(documentSpecification).log().all()
				.filter(document("path",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestParameters(
								parameterWithName("source").description("출발역 아이디"),
								parameterWithName("target").description("도착역 아이디")
						),
						responseFields(
								subsectionWithPath("stations[]").description("출발역부터 도착역까지 역 경로"),
								fieldWithPath("distance").description("출발역과 도착역의 거리")
						)
				));
	}

	public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청_문서화(Long source, Long target, RequestSpecification spec) {
		return requestPath(source, target, createRestAssuredWithDocument(spec));
	}

	public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance) {
		Map<String, String> lineCreateParams;
		lineCreateParams = new HashMap<>();
		lineCreateParams.put("name", name);
		lineCreateParams.put("color", color);
		lineCreateParams.put("upStationId", upStation + "");
		lineCreateParams.put("downStationId", downStation + "");
		lineCreateParams.put("distance", distance + "");

		return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
	}

	public static Map<String, String> 구간_파라미터_생성(Long upStationId, Long downStationId, int distance) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", upStationId + "");
		params.put("downStationId", downStationId + "");
		params.put("distance", distance + "");
		return params;
	}

	// 검증 메소드
	public static void 경로_조회됨(ExtractableResponse<Response> response, int distance, Long... stationsId) {
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationsId);
		assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
	}
}
