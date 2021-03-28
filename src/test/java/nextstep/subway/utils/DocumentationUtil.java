package nextstep.subway.utils;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class DocumentationUtil {
	public static RequestSpecification given(RequestSpecification spec,
		String identifier, RequestParametersSnippet requestSnippet,
		ResponseFieldsSnippet responseSnippet) {

		return RestAssured
			.given(spec).log().all()
			.filter(document(identifier,
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestSnippet, responseSnippet));
	}
}
