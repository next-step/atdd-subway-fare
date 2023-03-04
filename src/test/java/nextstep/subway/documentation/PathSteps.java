package nextstep.subway.documentation;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathSteps {
	public static ExtractableResponse<Response> 경로_조회_요청(RequestSpecification spec) {
		return RestAssured
			.given(spec).log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", 1L)
			.queryParam("target", 2L)
			.when().get("/paths")
			.then().log().all().extract();
	}
}
