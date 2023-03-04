package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static nextstep.member.domain.LoginMember.GUEST;

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
        AuthenticationPrincipal athenticationPrincipal = parameter.getParameterAnnotation(AuthenticationPrincipal.class);

        String authorizationHeader = webRequest.getHeader("Authorization");
        if (authorizationHeader == null && !athenticationPrincipal.required()) {
            return GUEST;
        }

        if (!"bearer".equalsIgnoreCase(authorizationHeader.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorizationHeader.split(" ")[1];

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(token));
        List<String> roles = jwtTokenProvider.getRoles(token);

        return new LoginMember(id, roles);
    }
}
