package nextstep.auth.presentation;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String BEARER_TYPE = "Bearer";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final int TOKEN_INDEX = 1;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AuthenticationPrincipal authenticationPrincipal = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        String authorization = webRequest.getHeader(AUTHORIZATION_HEADER);
        if (authorization == null || !authorization.startsWith(BEARER_TYPE)) {
            if (authenticationPrincipal.required()) {
                throw new AuthenticationException();
            }
            return LoginMember.createAnonymous();
        }

        String[] parts = authorization.split(" ");

        if (parts.length < 2 || !BEARER_TYPE.equalsIgnoreCase(parts[0])) {
            throw new AuthenticationException();
        }

        String token = parts[TOKEN_INDEX];
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String email = jwtTokenProvider.getPrincipal(token);
        return new LoginMember(email);
    }
}
