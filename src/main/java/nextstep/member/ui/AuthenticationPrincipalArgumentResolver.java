package nextstep.member.ui;

import nextstep.exception.UnAuthorizedException;
import nextstep.member.application.AuthService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.Guest;
import nextstep.member.domain.Member;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        if (Objects.isNull(authorization)) {
            return new Guest();
        }
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new UnAuthorizedException();
        }
        String token = authorization.split(" ")[1];
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(token));
        return authService.findMemberById(id);
    }
}
