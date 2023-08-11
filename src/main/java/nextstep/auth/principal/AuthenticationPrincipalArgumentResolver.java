package nextstep.auth.principal;

import java.util.Optional;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_DELIMITER = " ";
    private static final int EXPECTED_TOKEN_PARTS = 2;
    public static final int TOKEN_TYPE_INDEX = 0;
    public static final int TOKEN_INDEX = 1;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Optional<String> token = getToken(webRequest);
        if (token.isEmpty()) {
            return UserPrincipal.createAnonymous();
        }

        String username = jwtTokenProvider.getPrincipal(token.get());
        String role = jwtTokenProvider.getRoles(token.get());

        return new UserPrincipal(username, role);
    }

    private Optional<String> getToken(NativeWebRequest webRequest) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return Optional.empty();
        }
        String[] tokenInfo = authorization.split(TOKEN_DELIMITER);
        if (isBearerToken(tokenInfo)) {
            return Optional.empty();
        }
        return Optional.of(tokenInfo[TOKEN_INDEX]);
    }

    private static boolean isBearerToken(String[] tokenInfo) {
        return tokenInfo.length != EXPECTED_TOKEN_PARTS || !"bearer".equalsIgnoreCase(tokenInfo[TOKEN_TYPE_INDEX]);
    }
}
