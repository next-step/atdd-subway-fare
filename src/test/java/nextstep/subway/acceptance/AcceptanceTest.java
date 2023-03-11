package nextstep.subway.acceptance;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.utils.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    protected RequestSpecification spec;

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    String 관리자;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

}
