package nextstep.auth.ui;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.member.domain.AnonymousLoginMember;
import nextstep.member.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class)
                || parameter.hasParameterAnnotation(AuthenticationLogin.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (parameter.hasParameterAnnotation(AuthenticationLogin.class)) {
            return resolveAuthenticationLogin(webRequest);
        }

        return resolveAuthenticationPrincipal(webRequest);
    }

    private LoginMember resolveAuthenticationLogin(NativeWebRequest webRequest) {
        String authorization = webRequest.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return new AnonymousLoginMember();
        }

        String token = authorization.split(" ")[1];
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String email = jwtTokenProvider.getPrincipal(token);
        return new LoginMember(email);
    }

    private LoginMember resolveAuthenticationPrincipal(NativeWebRequest webRequest) {
        String authorization = webRequest.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthenticationException();
        }

        String token = authorization.split(" ")[1];
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String email = jwtTokenProvider.getPrincipal(token);
        return new LoginMember(email);
    }
}

