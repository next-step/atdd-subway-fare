package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

    public static RequestSpecification getSpec(RequestSpecification spec, String identifier,
                                               RequestParametersSnippet request, ResponseFieldsSnippet response) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(identifier,
                        getRequestPreprocessor(), getResponsePreprocessor(),
                        request, response));
    }

    public static OperationResponsePreprocessor getResponsePreprocessor() {
        return preprocessResponse(prettyPrint());
    }

    public static OperationRequestPreprocessor getRequestPreprocessor() {
        return preprocessRequest(prettyPrint());
    }
}
