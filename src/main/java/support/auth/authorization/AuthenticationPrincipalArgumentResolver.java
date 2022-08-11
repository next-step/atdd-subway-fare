package support.auth.authorization;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;
import support.auth.userdetails.User;

import java.util.Objects;

import static support.auth.userdetails.User.MockUser;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean noLoginUser = Objects.requireNonNull(parameter.getParameterAnnotation(AuthenticationPrincipal.class).noLoginUser());

        if (noLoginUser && authentication == null) {
            return MockUser();
        }

        return new User(authentication.getPrincipal().toString(), null, authentication.getAuthorities(), authentication.getAge());
    }
}
