package nextstep.member.ui;

import nextstep.member.domain.OptionalAuthenticationPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class OptionalAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    public OptionalAuthenticationPrincipalArgumentResolver(AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver) {
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalAuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        if (authorization != null) {
            return authenticationPrincipalArgumentResolver.createLoginMember(authorization);
        }
        return null;
    }
}
