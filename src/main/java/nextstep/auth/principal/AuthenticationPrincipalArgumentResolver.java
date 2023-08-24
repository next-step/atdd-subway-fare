package nextstep.auth.principal;

import nextstep.auth.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

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
        AuthenticationPrincipal authenticationPrincipal = Objects.requireNonNull(parameter.getParameterAnnotation(AuthenticationPrincipal.class));

        try {
            return createUserPrincipal(webRequest);
        } catch (AuthenticationException e) {
            if (authenticationPrincipal.required()) {
                throw e;
            }

            return new UnknownUserPrincipal();
        }
    }

    private UserPrincipal createUserPrincipal(NativeWebRequest webRequest) {
        String authorization = webRequest.getHeader("Authorization");
        if (authorization == null || !"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        String username = jwtTokenProvider.getPrincipal(token);
        String role = jwtTokenProvider.getRoles(token);

        return new LoggedInUserPrincipal(username, role);
    }
}
