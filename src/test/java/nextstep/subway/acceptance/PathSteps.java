package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

	public static ExtractableResponse<Response> searchPath(RequestSpecification spec, Long source, Long target) {
		Map<String, Long> params = new HashMap<>();
		params.put("source", source);
		params.put("target", target);

		ExtractableResponse<Response> searchResponse = RestAssured
				.given(spec).log().all()
				.filter(document("path",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint())))
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.params(params)
				.when().get("/paths")
				.then().log().all()
				.extract();
		return searchResponse;
	}
}
