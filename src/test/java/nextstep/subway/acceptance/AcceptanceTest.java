package nextstep.subway.acceptance;

import nextstep.subway.utils.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest extends ApplicationTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";

    String 관리자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        관리자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }
}
