package nextstep.subway.common;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DocumentationUtils {

    private DocumentationUtils() {
    }

    public static RequestSpecification given(RequestSpecification spec,
                                             String documentName,
                                             RequestParametersSnippet requestParametersSnippet,
                                             ResponseFieldsSnippet responseFieldsSnippet) {
        return RestAssured.given(spec).log().all()
                .filter(document(documentName,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParametersSnippet,
                        responseFieldsSnippet));
    }
}
