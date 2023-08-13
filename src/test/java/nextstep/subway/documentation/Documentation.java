package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.documentation.path.PathDocumentationStep.authorizationHeaderSnippet;
import static nextstep.subway.documentation.path.PathDocumentationStep.authorizationHeaderSnippet;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class Documentation {
    protected RequestSpecification spec;
    @LocalServerPort
    private int port;
    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
      RestAssured.port = port;
      this.spec = new RequestSpecBuilder()
              .addFilter(documentationConfiguration(restDocumentation))
              .build();
    }

    public RequestSpecification getSpecification(String identifier, RequestParametersSnippet request, ResponseFieldsSnippet response){
      return spec.filter(
          document(
              identifier,
              preprocessRequest(prettyPrint()),
              preprocessResponse(prettyPrint()),
              authorizationHeaderSnippet(),
              request,
              response
          ));
    }
}
