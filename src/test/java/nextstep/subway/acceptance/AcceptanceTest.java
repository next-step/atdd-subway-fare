package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.utils.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.utils.GithubResponses.사용자5;
import static nextstep.subway.utils.GithubResponses.사용자6;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @LocalServerPort
    private int port;

    String 관리자;
    String 청소년;
    String 어린이;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        청소년 = 베어러_인증_로그인_요청(사용자5.getEmail(), PASSWORD).jsonPath().getString("accessToken");
        어린이 = 베어러_인증_로그인_요청(사용자6.getEmail(), PASSWORD).jsonPath().getString("accessToken");
    }
}
