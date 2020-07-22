package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.config.LoginInterceptorConfig;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.UsernamePasswordAuthenticationInterceptor;
import nextstep.subway.auth.utils.TestUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(LoginInterceptorConfig.class)
class UsernamePasswordAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private UsernamePasswordAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new TestUserDetails());

        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle() throws IOException {
        // when
        interceptor.preHandle(request, response, new Object());

        // then
        HttpSession httpSession = request.getSession();
        assertThat(httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
    }

    @Test
    void convert() {
        // when
        AuthenticationToken authenticationToken = interceptor.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void attemptAuthentication() {
        // when
        Authentication authentication = interceptor.attemptAuthentication(request, response);

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getPrincipal()).isEqualTo(EMAIL);
        assertThat(((UserDetails) authentication.getPrincipal()).getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        request.setParameter("username", EMAIL);
        request.setParameter("password", PASSWORD);
        return request;
    }
}