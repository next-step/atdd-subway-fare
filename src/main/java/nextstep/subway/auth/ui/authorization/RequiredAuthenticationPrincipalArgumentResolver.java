package nextstep.subway.auth.ui.authorization;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.OptionalAuthenticationPrincipal;
import nextstep.subway.auth.exception.InvalidAuthenticationException;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

public class RequiredAuthenticationPrincipalArgumentResolver extends AuthenticationPrincipalArgumentResolver {

    @Override
    public Class<AuthenticationPrincipal> getAuthenticationAnnotation() {
        return AuthenticationPrincipal.class;
    }

    @Override
    public Object getPrincipalByAuthentication(MethodParameter parameter, Authentication authentication) {
        if (authentication == null) {
            throw new InvalidAuthenticationException();
        }
        if (authentication.getPrincipal() instanceof Map) {
            return extractPrincipal(parameter, authentication);
        }

        return authentication.getPrincipal();
    }
}
