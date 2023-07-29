package nextstep.marker;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Retention(RetentionPolicy.RUNTIME)
@Import({Documentation.DocumentationSetter.class})
@ExtendWith(RestDocumentationExtension.class)
public @interface Documentation {

    @TestComponent
    class DocumentationSetter implements ApplicationListener<ServletWebServerInitializedEvent> {
        protected RequestSpecification spec;

        @Override
        public void onApplicationEvent(ServletWebServerInitializedEvent event) {
            RestAssured.port = event.getWebServer().getPort();
        }

        @BeforeEach
        public void setUp(RestDocumentationContextProvider restDocumentation) {
            this.spec = new RequestSpecBuilder()
                    .addFilter(documentationConfiguration(restDocumentation))
                    .build();
        }
    }
}
