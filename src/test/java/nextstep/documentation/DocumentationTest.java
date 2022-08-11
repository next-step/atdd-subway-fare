package nextstep.documentation;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import nextstep.DataLoader;
import nextstep.acceptance.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.acceptance.steps.MemberSteps.로그인_되어_있음;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class DocumentationTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private DataLoader dataLoader;

    protected RequestSpecification spec;

    String 관리자;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();
        spec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation)).build();
        관리자 = 로그인_되어_있음(EMAIL, PASSWORD);
    }
}
