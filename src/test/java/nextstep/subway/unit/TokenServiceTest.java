package nextstep.subway.unit;

import nextstep.common.exception.PasswordMatchException;
import nextstep.member.application.TokenService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.acceptance.ApplicationContextTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.common.error.MemberError.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("인증 기능 테스트")
@Transactional
class TokenServiceTest extends ApplicationContextTest {

    private static final String USER = "user@email.com";
    private static final String PASSWORD = "password";
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        memberRepository.save(createMember(USER, PASSWORD, 10));
    }

    @DisplayName("로그인 성공한다.")
    @Test
    void success_loginTest() {
        final TokenResponse tokenResponse = tokenService.createToken(USER, PASSWORD);

        assertAll(
                () -> assertThat(tokenResponse.getAccessToken()).isNotNull()
        );
    }

    @DisplayName("로그인 시 패스워드가 다르다.")
    @Test
    void error_loginTest() {
        assertThatThrownBy(() -> tokenService.createToken(USER, "different password"))
                .isInstanceOf(PasswordMatchException.class)
                .hasMessage(UNAUTHORIZED.getMessage());
    }

    private Member createMember(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }
}