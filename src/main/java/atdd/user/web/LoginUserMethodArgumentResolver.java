package atdd.user.web;

import atdd.user.domain.User;
import atdd.user.application.UserService;
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
    private final UserService userService;

    public LoginUserMethodArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = (String) webRequest.getAttribute("loginUserEmail", SCOPE_REQUEST);
        if (Strings.isBlank(email)) {
            return new User();
        }

        try {
            return userService.findUserByEmail(email);
        } catch (Exception e) {
            return new User();
        }
    }
}
