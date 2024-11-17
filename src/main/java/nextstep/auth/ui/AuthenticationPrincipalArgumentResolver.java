package nextstep.auth.ui;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.domain.GuestMember;
import nextstep.auth.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
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

        AuthenticationPrincipal annotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        String authorization = webRequest.getHeader("Authorization");

        // 비로그인 유저 요청을 허용한 경우 인증 헤더가 null인 경우만 비회원 객체를 반환
        if (annotation.acceptGuestRequest()) {
            if (authorization == null) {
                return new GuestMember();
            }
        }

        if (authorization == null) {
            throw new AuthenticationException();
        }

        String autorizationType = authorization.split(" ")[0];
        if (!"bearer".equalsIgnoreCase(autorizationType)) {
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
