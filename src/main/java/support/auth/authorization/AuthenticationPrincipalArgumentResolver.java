package support.auth.authorization;

import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;
import support.auth.userdetails.User;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final boolean member = parameter.getParameterAnnotation(AuthenticationPrincipal.class).isMember();

        if (!member && Objects.isNull(authentication)) {
            return User.nonLoginUser();
        }

        return new User(authentication.getPrincipal().toString(), null, authentication.getAuthorities(), authentication.getAge());
    }
}
