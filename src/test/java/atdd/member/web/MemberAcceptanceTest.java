package atdd.member.web;

import atdd.AbstractAcceptanceTest;
import atdd.login.web.LoginHttpTest;
import atdd.member.application.dto.MemberResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static atdd.TestConstant.*;
import static atdd.member.web.MemberController.MEMBER_URL;
import static atdd.security.JwtTokenProvider.TOKEN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AbstractAcceptanceTest {

    private MemberHttpTest memberHttpTest;
    private LoginHttpTest loginHttpTest;

    @BeforeEach
    void setUp() {
        memberHttpTest = new MemberHttpTest(webTestClient);
        loginHttpTest = new LoginHttpTest(webTestClient);
    }

    @DisplayName("회원 가입을 할 수 있다")
    @Test
    void beAbleToJoin() {
        MemberResponseView view = memberHttpTest.createMember(TEST_MEMBER);

        assertThat(view).isNotNull();
        assertThat(view.getEmail()).isEqualTo(TEST_MEMBER_EMAIL);
        assertThat(view.getName()).isEqualTo(TEST_MEMBER_NAME);
        assertThat(view.getPassword()).isEqualTo(TEST_MEMBER_PASSWORD);
    }

    @DisplayName("회원 탈퇴를 할 수 있다")
    @Test
    void beAbleToWithdrawal() {
        MemberResponseView view = memberHttpTest.createMember(TEST_MEMBER);

        webTestClient.delete().uri(MEMBER_URL + "/" + view.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
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

    @DisplayName("회원 자신의 정보를 조회 할 수 있다")
    @Test
    void beAbleToFindMe() {
        memberHttpTest.createMemberRequest(TEST_MEMBER);

        String token = loginHttpTest.loginMember(TEST_MEMBER);

        webTestClient.get().uri(MEMBER_URL + "/me")
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.email").isEqualTo(TEST_MEMBER_EMAIL)
                .jsonPath("$.name").isEqualTo(TEST_MEMBER_NAME);

    }

}