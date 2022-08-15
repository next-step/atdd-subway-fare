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

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int DEFAULT_AGE = 20;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrincipal authenticationPrincipal = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        boolean required = Objects.requireNonNull(authenticationPrincipal).required();

        if (!required) {
            return new User(null, null, DEFAULT_AGE, null);
        }

        return new User(authentication.getPrincipal().toString(), null, authentication.getAge(), authentication.getAuthorities());

    }
}
