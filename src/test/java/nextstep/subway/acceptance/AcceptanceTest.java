package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String CHILD_EMAIL = "child@email.com";
    private static final String TEENAGER_EMAIL = "teenager@email.com";
    private static final String PASSWORD = "password";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private DataLoader dataLoader;

    String 관리자;
    String 어린이;
    String 청소년;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 로그인_되어_있음(ADMIN_EMAIL, PASSWORD);
        어린이 = 로그인_되어_있음(CHILD_EMAIL, PASSWORD);
        청소년 = 로그인_되어_있음(TEENAGER_EMAIL, PASSWORD);
    }
}
