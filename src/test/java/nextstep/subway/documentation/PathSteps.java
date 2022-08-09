package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
	public static RequestSpecification given(RequestSpecification spec, String identifier) {
		return RestAssured
				.given(spec).log().all()
				.filter(document(identifier,
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint())));
	}
}
