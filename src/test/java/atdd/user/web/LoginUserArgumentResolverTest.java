package atdd.user.web;

import atdd.TestConstant;
import atdd.user.domain.User;
import atdd.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = LoginUserMethodArgumentResolver.class)
public class LoginUserArgumentResolverTest {

    @Autowired
    private LoginUserMethodArgumentResolver loginUserArgumentResolver;

    @MockBean
    private UserRepository userRepository;

    @Test
    void resolveArgument() throws Exception {
        // given
        Method defaultsMethod = UserController.class.getMethod("retrieveUser", User.class);
        MethodParameter parameter = new MethodParameter(defaultsMethod, 0);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("loginUserEmail", TestConstant.TEST_USER_EMAIL);
        NativeWebRequest webRequest = new ServletWebRequest(mockRequest);
        given(userRepository.findUserByEmail(any()))
                .willReturn(User.builder()
                        .email(TestConstant.TEST_USER_EMAIL)
                        .name(TestConstant.TEST_USER_NAME)
                        .password(TestConstant.TEST_USER_PASSWORD).build());

        // when
        Object argument = loginUserArgumentResolver.resolveArgument(parameter, null, webRequest, null);

        // then
        assertThat(argument instanceof User).isEqualTo(true);
        User loginUser = (User) argument;
        assertThat(loginUser.getName()).isEqualTo(TestConstant.TEST_USER_NAME);
    }
}