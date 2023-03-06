package nextstep.subway.documentation;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathSteps {
	public static ExtractableResponse<Response> 최단_거리_경로_조회_요청(RequestSpecification spec) {
		return RestAssured
			.given(spec).log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", 1L)
			.queryParam("target", 2L)
			.queryParam("type", "DISTANCE")
			.when().get("/paths")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 최소_시간_경로_조회_요청(RequestSpecification spec) {
		return RestAssured
			.given(spec).log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", 1L)
			.queryParam("target", 2L)
			.queryParam("type", "DURATION")
			.when().get("/paths")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 출발역과_도착역이_동일하게_최단_거리_경로_조회_요청(RequestSpecification spec) {
		return RestAssured
			.given(spec).log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", 1L)
			.queryParam("target", 1L)
			.queryParam("type", "DISTANCE")
			.when().get("/paths")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 출발역과_도착역이_동일하게_최소_시간_경로_조회_요청(RequestSpecification spec) {
		return RestAssured
			.given(spec).log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", 1L)
			.queryParam("target", 1L)
			.queryParam("type", "DURATION")
			.when().get("/paths")
			.then().log().all().extract();
	}
}
