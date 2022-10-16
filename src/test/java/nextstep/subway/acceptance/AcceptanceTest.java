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
    private static final String EMAIL = "admin@email.com";
    private static final String CHILDREN = "children@email.com";
    private static final String TEENAGER = "teenager@email.com";
    private static final String ADULT = "adult@email.com";
    private static final String PASSWORD = "password";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private DataLoader dataLoader;

    String 관리자;
    String 청소년;
    String 어린이;
    String 성인;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 로그인_되어_있음(EMAIL, PASSWORD);
        청소년 = 로그인_되어_있음(TEENAGER, PASSWORD);
        어린이 = 로그인_되어_있음(CHILDREN, PASSWORD);
        성인 = 로그인_되어_있음(ADULT, PASSWORD);
    }
}
