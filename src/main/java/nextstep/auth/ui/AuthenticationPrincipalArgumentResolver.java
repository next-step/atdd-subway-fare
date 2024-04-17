package nextstep.auth.ui;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.TokenInfo;
import nextstep.member.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String AUTH_PREFIX = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(AUTHORIZATION);

        if (StringUtils.hasLength(authorization) && authorization.startsWith(AUTH_PREFIX)) {
            String token = authorization.substring(AUTH_PREFIX.length());
            TokenInfo tokenInfo = jwtTokenProvider.getPrincipal(token);
            return new LoginMember(tokenInfo.getEmail(), tokenInfo.getAge());
        }

        if (isAuthRequired(parameter)) {
            throw new AuthenticationException();
        }

        return new LoginMember(null, null);
    }

    private boolean isAuthRequired(final MethodParameter parameter) {
        final AuthenticationPrincipal annotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        return Optional.ofNullable(annotation)
                .map(AuthenticationPrincipal::required)
                .orElse(true);
    }
}
