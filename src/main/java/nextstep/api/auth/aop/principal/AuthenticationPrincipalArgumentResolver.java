package nextstep.api.auth.aop.principal;

import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.AuthenticationException;
import nextstep.api.auth.support.JwtTokenProvider;

@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final var annotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        final var authorizationHeader = Optional.ofNullable(webRequest.getHeader(HttpHeaders.AUTHORIZATION));

        if (authorizationHeader.isEmpty() && !annotation.required()) {
            return new AnonymousPrincipal();
        }

        final var bearerToken = authorizationHeader.orElseThrow(AuthenticationException::new);
        final var token = extractBearerToken(bearerToken);
        return jwtTokenProvider.getUserPrincipal(token);
    }

    private String extractBearerToken(final String bearerToken) {
        final var parts = bearerToken.split(" ");
        if (parts.length < 2 && !"bearer".equalsIgnoreCase(parts[0])) {
            throw new AuthenticationException();
        }
        return parts[1];
    }
}
