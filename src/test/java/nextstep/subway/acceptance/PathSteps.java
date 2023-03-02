package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

    public static RequestSpecification basicDocumentRequest(final RequestSpecification spec,
                                                            final String path) {
        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document(path,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
