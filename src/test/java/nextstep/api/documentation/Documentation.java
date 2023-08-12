package nextstep.api.documentation;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class Documentation {

    private static final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private static final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @LocalServerPort
    private Integer port;
    protected RequestSpecification spec;
    @Value("${enabled.logging}")
    private boolean enableLogging;

    @BeforeEach
    public void setUp(final RestDocumentationContextProvider restDocumentation) {
        initPort();
        logRestAssured();
        initSpec(restDocumentation);
    }

    private void initPort() {
        RestAssured.port = port;
    }

    private void logRestAssured() {
        if (enableLogging) {
            RestAssured.filters(requestLoggingFilter, responseLoggingFilter);
        }
    }

    private void initSpec(final RestDocumentationContextProvider restDocumentation) {
        final var requestSpecBuilder = new RequestSpecBuilder().addFilter(
                documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris().removePort(), prettyPrint())
                        .withResponseDefaults(prettyPrint())
        );

        this.spec = RestAssured.given(requestSpecBuilder.build());
    }

    protected RequestSpecification makeRequestSpec(final RestDocumentationFilter documentFilter) {
        return RestAssured.given(spec).filter(documentFilter);
    }
}
