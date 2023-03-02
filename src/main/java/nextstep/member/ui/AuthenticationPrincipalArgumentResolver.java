package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.Optional;

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
        String authorization = webRequest.getHeader("Authorization");
        boolean required = parameter.getParameterAnnotation(AuthenticationPrincipal.class).required();

        if (!required && Strings.isEmpty(authorization)) {
            return Optional.ofNullable(null);
        }

        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(token));
        List<String> roles = jwtTokenProvider.getRoles(token);

        return loginMemberByRequired(required, id, roles);
    }

    private Object loginMemberByRequired(boolean required, Long id, List<String> roles) {
        if (!required) {
            return Optional.of(new LoginMember(id, roles));
        }

        return new LoginMember(id, roles);
    }

    private boolean isOptionalAuth(AuthenticationPrincipal authenticationPrincipal, String authorization) {
        if (!authenticationPrincipal.required()) {
            return Strings.isEmpty(authorization);
        }

        return false;
    }
}
