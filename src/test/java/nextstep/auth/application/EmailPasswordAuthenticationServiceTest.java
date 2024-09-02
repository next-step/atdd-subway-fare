package nextstep.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import io.cucumber.java.sl.In;
import nextstep.auth.application.EmailPasswordAuthenticationService;
import nextstep.auth.application.UserDetailsService;
import nextstep.auth.application.dto.EmailPasswordAuthRequest;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.dto.TokenResponse;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.infrastructure.token.TokenGenerator;
import nextstep.member.auth.MemberUserDetails;
import nextstep.member.domain.Member;
import nextstep.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailPasswordAuthenticationServiceTest {

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrongpassword";
    private static final String TOKEN = "generated.token";
    private static final Integer AGE = 10;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenGenerator tokenGenerator;

    @InjectMocks
    private EmailPasswordAuthenticationService authService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        Member member = new Member(EMAIL, PASSWORD, AGE);
        userDetails = new MemberUserDetails(member);
    }

    @Test
    @DisplayName("올바른 이메일과 비밀번호로 인증 성공")
    void authenticateSuccess() {
        // Given
        EmailPasswordAuthRequest request = new EmailPasswordAuthRequest(EMAIL, PASSWORD);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(tokenGenerator.createToken(EMAIL, AGE)).thenReturn(TOKEN);

        // When
        TokenResponse response = authService.authenticate(request);

        // Then
        assertThat(response.getAccessToken()).isEqualTo(TOKEN);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 인증 실패")
    void authenticateFailureInvalidPassword() {
        // Given
        EmailPasswordAuthRequest request = new EmailPasswordAuthRequest(EMAIL, WRONG_PASSWORD);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);

        // When & Then
        assertThrows(AuthenticationException.class, () -> authService.authenticate(request));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 인증 실패")
    void authenticateFailureUserNotFound() {
        // Given
        EmailPasswordAuthRequest request = new EmailPasswordAuthRequest(EMAIL, PASSWORD);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenThrow(new MemberNotFoundException("User not found"));

        // When & Then
        assertThrows(MemberNotFoundException.class, () -> authService.authenticate(request));
    }
}
