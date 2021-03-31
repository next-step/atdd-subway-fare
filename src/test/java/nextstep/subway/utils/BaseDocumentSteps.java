package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static nextstep.subway.utils.ApiDocumentUtils.getDocumentRequest;
import static nextstep.subway.utils.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public abstract class BaseDocumentSteps {

    private final RequestSpecification spec;

    public BaseDocumentSteps(RequestSpecification spec) {
        this.spec = spec;
    }

    public static RequestSpecification givenDefault() {
        return RestAssured
                .given().log().all();
    }

    private RequestSpecification givenAddSpec() {
        return RestAssured
                .given(spec).log().all();
    }

    public RequestSpecification requestDocumentOfDefault(String identifier) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    public RequestSpecification requestDocumentOfFind(String identifier) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        initDocumentRequestParameters(),
                        initDocumentResponseBody()
                ));
    }

    public RequestSpecification requestDocumentOfAllType(String identifier) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        initDocumentRequestPathVariable(),
                        initDocumentRequestParameters(),
                        initDocumentRequestBody(),
                        initDocumentResponseBody()
                ));
    }

    public abstract PathParametersSnippet initDocumentRequestPathVariable();

    public abstract RequestParametersSnippet initDocumentRequestParameters();

    public abstract RequestFieldsSnippet initDocumentRequestBody();

    public abstract ResponseFieldsSnippet initDocumentResponseBody();
}
