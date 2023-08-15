package nextstep.api.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {

    private static final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private static final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @LocalServerPort
    private Integer port;
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;
    @Value("${enabled.logging}")
    private boolean enableLogging;

    @BeforeEach
    public void setUp() {
        initPort();
        logRestAssured();
        databaseCleanup.execute();
        dataLoader.loadData();
    }

    private void initPort() {
        RestAssured.port = port;
    }

    private void logRestAssured() {
        if (enableLogging) {
            RestAssured.filters(requestLoggingFilter, responseLoggingFilter);
        }
    }
}
