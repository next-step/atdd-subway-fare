package atdd.user.web;

import atdd.user.domain.User;
import atdd.user.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class LoginUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String LOGIN_USER_EMAIL = "loginUserEmail";

    private final UserRepository userRepository;

    public LoginUserMethodArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = (String) webRequest.getAttribute(LOGIN_USER_EMAIL, SCOPE_REQUEST);
        if (Strings.isBlank(email)) {
            return new User();
        }

        try {
            return userRepository.findUserByEmail(email);
        } catch (Exception e) {
            return new User();
        }
    }
}
