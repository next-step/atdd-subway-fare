package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

import io.restassured.RestAssured;
import nextstep.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private static final String YOUTH_EMAIL = "youth@email.com";
    private static final String CHILD_EMAIL = "child@email.com";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private DataLoader dataLoader;

    String 관리자;
    String 청소년;
    String 어린이;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 로그인_되어_있음(EMAIL, PASSWORD);
        청소년 = 로그인_되어_있음(YOUTH_EMAIL, PASSWORD);
        어린이 = 로그인_되어_있음(CHILD_EMAIL, PASSWORD);
    }
}
