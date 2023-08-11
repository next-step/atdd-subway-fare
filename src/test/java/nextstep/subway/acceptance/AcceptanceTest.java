package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.utils.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
    String 사용자_12세;
    String 사용자_13세;
    String 사용자_14세;
    String 사용자_19세;
    String 사용자_20세;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        사용자_12세 = 베어러_인증_로그인_요청(GithubResponses.사용자_12세.getEmail(), PASSWORD).jsonPath().getString("accessToken");
        사용자_13세 = 베어러_인증_로그인_요청(GithubResponses.사용자_13세.getEmail(), PASSWORD).jsonPath().getString("accessToken");
        사용자_14세 = 베어러_인증_로그인_요청(GithubResponses.사용자_14세.getEmail(), PASSWORD).jsonPath().getString("accessToken");
        사용자_19세 = 베어러_인증_로그인_요청(GithubResponses.사용자_19세.getEmail(), PASSWORD).jsonPath().getString("accessToken");
        사용자_20세 = 베어러_인증_로그인_요청(GithubResponses.사용자_20세.getEmail(), PASSWORD).jsonPath().getString("accessToken");
    }
}
