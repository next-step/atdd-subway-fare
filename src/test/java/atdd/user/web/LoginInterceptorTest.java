package atdd.user.web;

import atdd.TestConstant;
import atdd.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = JwtTokenProvider.class)
public class LoginInterceptorTest {

    private LoginInterceptor loginInterceptor;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        this.loginInterceptor = new LoginInterceptor(jwtTokenProvider);
    }

    @Test
    public void preHandle() {
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getUserEmail(any())).willReturn(TestConstant.TEST_USER_EMAIL);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/favorite/station");
        request.addHeader("Authorization", "Bearer ");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginInterceptor.preHandle(request, response, null);

        assertThat(request.getAttribute("loginUserEmail")).isEqualTo(TestConstant.TEST_USER_EMAIL);
    }
}
