package atdd;

import atdd.path.auth.JwtTokenProvider;
import atdd.path.auth.SignInInterceptor;
import atdd.path.auth.SignInResolver;
import atdd.path.configuration.WebMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(WebMvcTestConfig.class)
public class AbstractDocumentationTest {
    @MockBean
    public SignInInterceptor signInInterceptor;
    @MockBean
    public SignInResolver signInResolver;
    @MockBean
    public WebMvcConfig webMvcConfig;
    @MockBean
    public JwtTokenProvider jwtTokenProvider;

    public MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }


    public RestDocumentationResultHandler createDocumentationResultHandler(
            String identifier,
            RequestFieldsSnippet requestFieldsSnippet
    ) {
        return document(identifier, requestFieldsSnippet);
    }

    public RestDocumentationResultHandler createDocumentationResultHandler(
            String identifier,
            RequestFieldsSnippet requestFieldsSnippet,
            ResponseFieldsSnippet responseFieldsSnippet
    ) {
        return document(identifier, requestFieldsSnippet, responseFieldsSnippet);
    }

    public RestDocumentationResultHandler createDocumentationResultHandler(
            String identifier,
            PathParametersSnippet pathParametersSnippet,
            RequestFieldsSnippet requestFieldsSnippet
    ) {
        return document(identifier, pathParametersSnippet, requestFieldsSnippet);
    }

    public RestDocumentationResultHandler retrieveAllDocumentationResultHandler(
            String identifier,
            ResponseFieldsSnippet responseFieldsSnippet
    ) {
        return document(identifier,responseFieldsSnippet);
    }

    public RestDocumentationResultHandler retrieveDocumentationResultHandler(
            String identifier,
            PathParametersSnippet pathParametersSnippet,
            ResponseFieldsSnippet responseFieldsSnippet
    ) {
        return document(identifier, pathParametersSnippet, responseFieldsSnippet);
    }

    public RestDocumentationResultHandler deleteDocumentationResultHandler(
            String identifier,
            PathParametersSnippet pathParametersSnippet
    ) {
        return document(identifier, pathParametersSnippet);
    }

    public RestDocumentationResultHandler deleteDocumentationResultHandler(
            String identifier,
            PathParametersSnippet pathParametersSnippet,
            RequestParametersSnippet requestParametersSnippet
    ) {
        return document(identifier, pathParametersSnippet, requestParametersSnippet);
    }
}
