package nextstep.subway.documentation.given;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.restdocs.snippet.Snippet;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public interface SnippetTemplate {

    Snippet[] getSnippets();

    default RequestSpecification toGiven(RequestSpecification spec, String identifier) {
        RestDocumentationFilter document = RestAssuredRestDocumentation.document(
            identifier,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            getSnippets()
        );
        return RestAssured.given(spec).log().all()
                          .filter(document);
    }
}
