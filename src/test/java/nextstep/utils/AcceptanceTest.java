package nextstep.utils;

import io.restassured.RestAssured;
import nextstep.member.fixture.MemberAccounts;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.auth.AuthSteps.회원_토큰_생성;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;
    protected String 사용자1_토큰;
    protected String 청소년1_토큰;
    protected String 어린이1_토큰;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();

        Arrays.stream(MemberAccounts.values())
                .forEach(memberAccount -> {
                    회원_생성_요청(memberAccount.getEmail(), memberAccount.getPassword(), memberAccount.getAge());
                });

        final Map<String, String> tokenByEmail = Arrays.stream(MemberAccounts.values())
                .collect(Collectors.toMap(MemberAccounts::getEmail, memberAccount -> 회원_토큰_생성(memberAccount.getEmail(), memberAccount.getPassword())));

        사용자1_토큰 = tokenByEmail.get(MemberAccounts.사용자1.getEmail());
        청소년1_토큰 = tokenByEmail.get(MemberAccounts.청소년1.getEmail());
        어린이1_토큰 = tokenByEmail.get(MemberAccounts.어린이1.getEmail());
    }
}
