package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String ACCESS_TOKEN_OAUTH_TYPE = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (isAllowGuest(parameter) && authorization == null) {
            return null;
        }

        if (authorization == null || !isOAuthToken(authorization)) {
            throw new AuthenticationException();
        }

        String accessToken = authorization.substring(ACCESS_TOKEN_OAUTH_TYPE.length());
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(accessToken));
        List<String> roles = jwtTokenProvider.getRoles(accessToken);

        return new LoginMember(id, roles);
    }


    private boolean isOAuthToken(String authHeader) {
        return authHeader.startsWith(ACCESS_TOKEN_OAUTH_TYPE);
    }

    private boolean isAllowGuest(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthenticationPrincipal.class).allowGuest();
    }
}
