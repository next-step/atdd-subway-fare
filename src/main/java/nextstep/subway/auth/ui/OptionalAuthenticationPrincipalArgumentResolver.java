package nextstep.subway.auth.ui;

import nextstep.subway.auth.application.AuthManager;
import nextstep.subway.auth.application.provider.TokenType;
import nextstep.subway.auth.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class OptionalAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthManager authManager;

    public OptionalAuthenticationPrincipalArgumentResolver(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalAuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        if ((authorization == null)) {
            return new LoginMember();
        }

        TokenType tokenType = TokenType.findBy(authorization.split(" ")[0]);

        String token = authorization.split(" ")[1];
        String email = authManager.getPrincipal(token, tokenType);

        return new LoginMember(email);
    }
}
