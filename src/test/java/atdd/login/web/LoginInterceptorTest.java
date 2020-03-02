package atdd.login.web;

import atdd.member.web.LoginInterceptor;
import atdd.security.BearerTokenExtractor;
import atdd.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static atdd.TestConstant.TEST_MEMBER_EMAIL;
import static atdd.member.web.MemberController.MEMBER_URL;
import static atdd.security.JwtTokenProvider.TOKEN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {LoginInterceptor.class, JwtTokenProvider.class, BearerTokenExtractor.class})
public class LoginInterceptorTest {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void preHandle() {
        String token = jwtTokenProvider.createToken(TEST_MEMBER_EMAIL);

        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), MEMBER_URL + "/me");
        request.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        loginInterceptor.preHandle(request, response, null);

        assertThat(request.getAttribute("loginUserEmail")).isEqualTo(TEST_MEMBER_EMAIL);
    }

}
