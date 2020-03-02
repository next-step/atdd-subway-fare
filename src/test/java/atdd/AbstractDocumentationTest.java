package atdd;

import atdd.config.WebMvcConfig;
import atdd.member.web.LoginInterceptor;
import atdd.member.web.LoginUserMethodArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(WebMvcTestConfig.class)
public class AbstractDocumentationTest {
    @MockBean
    public LoginInterceptor loginInterceptor;
    @MockBean
    public LoginUserMethodArgumentResolver methodArgumentResolver;
    @MockBean
    public WebMvcConfig webMvcConfig;

    public MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    protected HeaderDescriptor getHeaderAuthorization() {
        return headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer auth credentials");
    }

    protected List<HeaderDescriptor> getHeaderAuthorizationAndContentType() {
        return List.of(getHeaderAuthorization(), getHeaderContentType());
    }

    protected List<HeaderDescriptor> getHeaderLocationAndContentType() {
        return List.of(headerWithName(HttpHeaders.LOCATION).description("Location header"), getHeaderContentType());
    }

    protected HeaderDescriptor getHeaderContentType() {
        return headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type");
    }

}
