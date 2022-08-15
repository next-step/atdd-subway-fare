package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.userdetails.GuestUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetails.User;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrincipal principal = parameter.getParameterAnnotation(AuthenticationPrincipal.class);

        assert principal != null;
        if (authentication == null && !principal.required()) {
            return new GuestUser();
        }

        return new User(
                authentication.getPrincipal().toString(),
                null,
                authentication.getAuthorities()
        );
    }
}
