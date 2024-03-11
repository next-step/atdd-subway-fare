package nextstep.core.auth.presentation;

import nextstep.core.auth.application.JwtTokenProvider;
import nextstep.core.auth.domain.LoginUser;
import nextstep.core.auth.domain.NonLoginUser;
import nextstep.core.auth.exception.InvalidTokenException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

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
        boolean required = annotation == null || annotation.required();

        String authorization = webRequest.getHeader("Authorization");
        if(!required) {
            if(authorization == null) {
                return new NonLoginUser();
            }
        }

        validateAuthorization(authorization);
        return new LoginUser(jwtTokenProvider.getPrincipal(getToken(authorization)));
    }

    private void validateAuthorization(String authorization) {
        if (authorization == null) {
            throw new InvalidTokenException("토큰이 전달되지 않았습니다.");
        }
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new InvalidTokenException("토큰 형식에 맞지 않습니다.");
        }
    }

    private String getToken(String authorization) {
        try {
            return authorization.split(" ")[1];
        } catch (Exception e) {
            throw new InvalidTokenException("토큰 형식에 맞지 않습니다");
        }
    }
}
