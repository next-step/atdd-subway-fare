package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;

import io.restassured.RestAssured;
import nextstep.subway.utils.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final String 청소년_EMAIL = "청소년@email.com";
    public static final String 청소년_PASSWORD = "password";
    public static final String 어린이_EMAIL = "어린이@email.com";
    public static final String 어린이_PASSWORD = "password";

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    String 관리자;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }
}
