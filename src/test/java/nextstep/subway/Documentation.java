package nextstep.subway;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import nextstep.subway.auth.utils.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@ExtendWith(RestDocumentationExtension.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
public class Documentation {
    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        RestAssuredMockMvc.webAppContextSetup(context, documentationConfiguration(restDocumentation));
    }

    protected static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .removePort(),
                prettyPrint());
    }

    protected static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }

    protected static HeaderDescriptor authorizationHeader(boolean optional) {
        HeaderDescriptor descriptor = headerWithName(HttpHeaders.AUTHORIZATION).description("사용자 인증 수단");
        if (optional) {
            descriptor.optional();
        }
        return descriptor;
    }
}
