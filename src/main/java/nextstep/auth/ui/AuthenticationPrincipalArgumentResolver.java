package nextstep.auth.ui;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.dto.TokenInfo;
import nextstep.auth.exception.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String AUTH_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        final String authorization = webRequest.getHeader("Authorization");

        if (StringUtils.hasLength(authorization) && authorization.startsWith(AUTH_PREFIX)) {
            final String token = authorization.substring(AUTH_PREFIX.length());
            final TokenInfo tokenInfo = jwtTokenProvider.getPrincipal(token);
            return new UserPrincipal(tokenInfo.getId(), tokenInfo.getEmail());
        }

        if (isAuthRequired(parameter)) {
            throw new AuthenticationException();
        }

        return new UserPrincipal(null, null);
    }

    private boolean isAuthRequired(final MethodParameter parameter) {
        final AuthenticationPrincipal annotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        return Optional.ofNullable(annotation)
                .map(AuthenticationPrincipal::required)
                .orElse(true);
    }

}
