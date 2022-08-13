package support.auth.authorization;

import nextstep.member.domain.RoleType;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;
import support.auth.userdetails.User;

import java.util.List;
import java.util.Objects;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean required = parameter.getParameterAnnotation(AuthenticationPrincipal.class).required();

        if(!required && Objects.equals(null, authentication)){
            return new User("guest@guest.com", null, 20, List.of(RoleType.ROLE_MEMBER.name()));
        }
        return new User(authentication.getPrincipal().toString(), null, authentication.getAge(),authentication.getAuthorities());
    }
}
