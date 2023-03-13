package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.NonLoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.Objects;

@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TYPE = "bearer";
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(AUTHORIZATION);
        if (existAuthRequest(authorization)) {
            return getLoginMember(authorization);
        }

        boolean required = Objects.requireNonNull(parameter.getParameterAnnotation(AuthenticationPrincipal.class)).required();
        if (!required) {
            return new NonLoginMember();
        }

        throw new AuthenticationException();
    }

    private LoginMember getLoginMember(String authorization) {
        if (!BEARER_TYPE.equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(token));
        int age = jwtTokenProvider.getAge(token);
        List<String> roles = jwtTokenProvider.getRoles(token);

        return new LoginMember(id, age, roles);
    }

    private boolean existAuthRequest(String authorization) {
        return Objects.nonNull(authorization);
    }
}
