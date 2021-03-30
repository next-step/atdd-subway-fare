package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.utils.ApiDocumentUtils.getDocumentRequest;
import static nextstep.subway.utils.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    protected static RequestSpecification spec;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        databaseCleanup.execute();

        spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    public static RequestSpecification givenDefault() {
        return RestAssured
                .given().log().all();
    }

    private static RequestSpecification givenAddSpec() {
        return RestAssured
                .given(spec).log().all();
    }

    public static RequestSpecification givenAndCreateDocumentForParameters(String identifier, RequestParametersSnippet requestParameters, ResponseFieldsSnippet responseFields) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters,
                        responseFields));
    }

    public static RequestSpecification givenAndCreateDocumentForPathAndParameters(String identifier, PathParametersSnippet pathVariable, RequestParametersSnippet requestParameters) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathVariable,
                        requestParameters));
    }

    public static RequestSpecification givenAndCreateDocumentForEmptyRequest(String identifier) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    public static RequestSpecification givenAndCreateDocumentForPathVariable(String identifier, PathParametersSnippet pathVariable) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathVariable));
    }

    public static RequestSpecification givenAndCreateDocumentForFields(String identifier, RequestFieldsSnippet requestBody, ResponseFieldsSnippet responseFields) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestBody,
                        responseFields));
    }

    public static RequestSpecification givenAndCreateDocumentForPathVariables(String identifier, PathParametersSnippet pathVariable, ResponseFieldsSnippet responseFields) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathVariable,
                        responseFields));
    }

    public static RequestSpecification givenAndCreateDocumentForAllRequestType(String identifier, PathParametersSnippet pathVariable, RequestFieldsSnippet requestBody, ResponseFieldsSnippet responseFields) {
        return givenAddSpec()
                .filter(document(identifier,
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathVariable,
                        requestBody,
                        responseFields));
    }
}
