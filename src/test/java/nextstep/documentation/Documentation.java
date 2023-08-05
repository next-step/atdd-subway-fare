package nextstep.documentation;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class Documentation {

    @LocalServerPort
    private Integer port;
    protected RequestSpecification spec;

    @BeforeEach
    public void setUp(final RestDocumentationContextProvider restDocumentation) {
        initPort();
        initSpec(restDocumentation);
    }

    private void initPort() {
        RestAssured.port = port;
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

    protected RequestSpecification makeRequestSpec(final String documentId) {
        return RestAssured.given(spec).filter(document(documentId));
    }
}
