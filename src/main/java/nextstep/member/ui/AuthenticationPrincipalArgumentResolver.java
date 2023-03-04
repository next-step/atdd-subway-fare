package nextstep.member.ui;

import java.util.List;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;

@Component
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
        boolean required = Objects.requireNonNull(parameter.getParameterAnnotation(AuthenticationPrincipal.class)).required();
        String authorization = webRequest.getHeader("Authorization");

        if (!required && authorization == null) {
            return new LoginMember(null, 20, List.of());
        }

        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(token));
        int age = jwtTokenProvider.getAge(token);
        List<String> roles = jwtTokenProvider.getRoles(token);

        return new LoginMember(id, age, roles);
    }
}
