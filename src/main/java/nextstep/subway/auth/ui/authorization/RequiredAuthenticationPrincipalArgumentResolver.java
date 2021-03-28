package nextstep.subway.auth.ui.authorization;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.exception.InvalidAuthenticationException;
import org.springframework.core.MethodParameter;

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
