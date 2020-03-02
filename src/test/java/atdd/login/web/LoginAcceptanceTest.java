package atdd.login.web;

import atdd.AbstractAcceptanceTest;
import atdd.member.web.MemberHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static atdd.TestConstant.TEST_MEMBER;
import static atdd.security.JwtTokenProvider.TOKEN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {

    private MemberHttpTest memberHttpTest;
    private LoginHttpTest loginHttpTest;

    @BeforeEach
    void setUp() {
        memberHttpTest = new MemberHttpTest(webTestClient);
        loginHttpTest = new LoginHttpTest(webTestClient);
    }

    @DisplayName("로그인을 할 수 있다")
    @Test
    void beAbleToLogin() {
        memberHttpTest.createMemberRequest(TEST_MEMBER);

        String authorization = loginHttpTest.loginMember(TEST_MEMBER);
        final String[] splits = authorization.split(" ");

        assertThat(authorization).isNotEmpty();
        assertThat(splits).hasSize(2);
        assertThat(splits[0]).isEqualTo(TOKEN_TYPE);
    }

}